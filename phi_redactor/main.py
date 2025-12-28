from fastapi import FastAPI
from pydantic import BaseModel
# Absolute import within the package
from phi_redactor_service.phi_redactor import PhiRedactor
import os
from dotenv import load_dotenv
load_dotenv()

# declare the application object of the FASTAPI
# app ohbject is the main point of interaction with the client browser
# server uses this to listen for client requests
app = FastAPI()
token = os.getenv("HUGGINGFACE_TOKEN")
print(token)
redactor = PhiRedactor(token=token, device=0)

# using pydantic for data parsing and validation
# basemodel is the base class for creating user defined models
# define the request model for the input
# the textrequest that java service sends contains a text string and a flag var
# we use this with request body in combination with POST
class TextRequest(BaseModel):
    text: str
    aggregate_redaction: bool = True

# define a response
class TextResponse(BaseModel):
    redacted_text: str

# define the response model- path operation mapped to /redact
# bind the function redact to /redact
# when /redact is called redeact_text is executed
# rescvies a POSTS request in form of TEXTRequest class
@app.post("/redact", response_model=TextResponse)
def redact_text(request: TextRequest):
    text = request.text
    redacted_text = redactor.mask_pii(text, aggregate_redaction=request.aggregate_redaction)
    return TextResponse(redacted_text=redacted_text)

