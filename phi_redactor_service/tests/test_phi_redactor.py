 #test_phi_redactor.py
import pytest
from phi_redactor_service.phi_redactor import PhiRedactor
import os
# create a redactor class before running each
@pytest.fixture
def redactor():
    # Use device=-1 for CPU in tests
    return PhiRedactor(token=os.getenv("HUGGINGFACE_TOKEN"), device=-1)

# tests the the redactor runs, not that it removes averything just the the token is used,
# model called, and returns some redacted text
def test_mask_pii_aggregated(redactor):
    text = "Patient John Doe, SSN 123-45-6789."
    redacted = redactor.mask_pii(text)
    assert "[REDACTED]" in redacted
    assert "John Doe" not in redacted

# tets that the text is masked sucessfully
def test_mask_pii_detailed(redactor):
    text = "Patient Jane Doe, SSN 123-45-6789."
    redacted = redactor.mask_pii(text, aggregate_redaction=False)
    assert "[FIRSTNAME]" in redacted or "[LASTNAME]" in redacted or "[SSN]" in redacted
    assert "Jane Doe" not in redacted and "123-45-6789" not in redacted
