from fastapi import FastAPI
from pydantic import BaseModel
from phi_redactor_service.phi_redactor import PhiRedactor
import os
from dotenv import load_dotenv
load_dotenv()

app = FastAPI()
token = os.getenv("HUGGINGFACE_TOKEN")
print(token)
redactor = PhiRedactor(token=token, device=0)

# define the request model for the input
class TextRequest(BaseModel):
    text: str
    aggregate_redaction: bool = True

class TextResponse(BaseModel):
    redacted_text: str

# define the response model
@app.post("/redact", response_model=TextResponse)
def redact_text(request: TextRequest):
    text = request.text
    redacted_text = redactor.mask_pii(text, aggregate_redaction=request.aggregate_redaction)
    return TextResponse(redacted_text=redacted_text)

