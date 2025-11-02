 #test_phi_redactor.py
import pytest
from phi_redactor_service.phi_redactor import PhiRedactor

@pytest.fixture
def redactor():
    # Use device=-1 for CPU in tests
    return PhiRedactor(token="YOUR_TOKEN_HERE", device=-1)

def test_mask_pii_aggregated(redactor):
    text = "Patient John Doe, SSN 123-45-6789."
    redacted = redactor.mask_pii(text)
    assert "[REDACTED]" in redacted
    assert "John Doe" not in redacted

def test_mask_pii_detailed(redactor):
    text = "Patient John Doe, SSN 123-45-6789."
    redacted = redactor.mask_pii(text, aggregate_redaction=False)
    assert "[FIRSTNAME]" in redacted or "[LASTNAME]" in redacted or "[SSN]" in redacted
    assert "John Doe" not in redacted and "123-45-6789" not in redacted
