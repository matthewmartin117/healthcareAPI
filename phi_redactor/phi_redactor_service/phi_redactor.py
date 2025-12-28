# phi_redactor.py
from transformers import pipeline, AutoModelForTokenClassification, AutoTokenizer
import torch
import os

token = os.getenv("HUGGINGFACE_TOKEN")
class PhiRedactor:
    def __init__(self, model_name="ab-ai/pii_model", token=token, device=0):
        self.model_name = model_name
        self.token = token
        self.device = device
        self.model = AutoModelForTokenClassification.from_pretrained(model_name, use_auth_token=token)
        self.tokenizer = AutoTokenizer.from_pretrained(model_name, use_auth_token=token)
        self.ner_pipeline = pipeline(
            "ner",
            model=self.model,
            tokenizer=self.tokenizer,
            aggregation_strategy="simple",
            device=device
        )

    def classify_text(self, text):
        return self.ner_pipeline(text)

    def mask_pii(self, text, aggregate_redaction=True):
        results = self.ner_pipeline(text)
        masked_text = text
        for ent in sorted(results, key=lambda x: x['start'], reverse=True):
            start, end = ent['start'], ent['end']
            label = ent['entity_group']
            replacement = "[REDACTED]" if aggregate_redaction else f"[{label}]"
            masked_text = masked_text[:start] + replacement + masked_text[end:]
        return masked_text
