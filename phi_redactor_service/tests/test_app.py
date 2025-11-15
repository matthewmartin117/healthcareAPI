from fastapi.testclient import TestClient
from phi_redactor_service.main import app

client = TestClient(app)

def test_redact_single_phi():
    text = {"text": "Patient John Doe, SSN 123-45-6789."}
    response = client.post("/redact", json=text)
    assert response.status_code == 200
    data = response.json()
    assert "redacted_text" in data
    assert "John Doe" not in data["redacted_text"]
    assert "123-45-6789" not in data["redacted_text"]

def test_redact_multiple_phi():
    text = {
        "text": "Patient Jane Smith, DOB 01/01/1980, email jane.smith@example.com, phone 555-123-4567."
    }
    response = client.post("/redact", json=text)
    data = response.json()
    assert response.status_code == 200
    redacted = data["redacted_text"]
    # ensure all PHI is removed
    for phi in ["Jane Smith", "01/01/1980", "jane.smith@example.com", "555-123-4567"]:
        assert phi not in redacted

def test_redact_empty_text():
    text = {"text": ""}
    response = client.post("/redact", json=text)
    data = response.json()
    assert response.status_code == 200
    assert data["redacted_text"] == ""

def test_redact_no_phi_text():
    text = {"text": "This is a note without any PHI."}
    response = client.post("/redact", json=text)
    data = response.json()
    assert response.status_code == 200
    # text should remain unchanged if no PHI
    assert data["redacted_text"] == text["text"]

def test_redact_repeated_phi():
    text = {"text": "John Doe is seen. John Doe has no complaints."}
    response = client.post("/redact", json=text)
    data = response.json()
    redacted = data["redacted_text"]
    assert "John Doe" not in redacted
    # make sure rest of the sentence still makes sense (optional)
    assert "has no complaints" in redacted

def test_invalid_json():
    # sending invalid JSON structure
    response = client.post("/redact", data="not a json")
    assert response.status_code == 422  # FastAPI returns 422 Unprocessable Entity

def test_missing_text_key():
    # JSON without "text" key
    response = client.post("/redact", json={"wrong_key": "something"})
    assert response.status_code == 422
