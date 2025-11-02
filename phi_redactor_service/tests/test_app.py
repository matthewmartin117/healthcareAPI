# test_app.py
from fastapi.testclient import TestClient
from phi_redactor_service.main import app

client = TestClient(app)

def test_redact_endpoint():
    text = "Patient John Doe, SSN 123-45-6789."
    response = client.post("/redact", json={"text": text})
    assert response.status_code == 200
    data = response.json()
    assert "redacted_text" in data
    assert "John Doe" not in data["redacted_text"]
