CREATE TABLE IF NOT EXISTS contact_information (
  patient_patientid VARCHAR(64) NOT NULL,
  contact_type VARCHAR(255),
  contact_val VARCHAR(255),
  PRIMARY KEY (patient_patientid, contact_type)
);

-- Ensure patients table exists with patientID primary key
CREATE TABLE IF NOT EXISTS patients (
  patientID VARCHAR(64) PRIMARY KEY,
  name VARCHAR(255),
  dateOfBirth DATE
);

CREATE TABLE IF NOT EXISTS biological_samples (
  Id VARCHAR(64) PRIMARY KEY,
  sampleType VARCHAR(255),
  collectionDate TIMESTAMP,
  reasonCollected VARCHAR(255),
  patient_id VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS clinical_notes (
  id VARCHAR(64) PRIMARY KEY,
  note TEXT,
  noteDate TIMESTAMP,
  patient_id VARCHAR(64)
);
