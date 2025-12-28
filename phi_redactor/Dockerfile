# LOCATION: /phi-redactor/Dockerfile (Root folder)
FROM python:3.11-slim

WORKDIR /app

# Copy the requirements file from the subfolder first (for cache efficiency)
COPY phi_redactor_service/requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the ENTIRE project (main.py AND the phi_redactor_service folder)
COPY . .

# Expose FastAPI port
EXPOSE 8000

# Run FastAPI
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]