-- Add recorded_by_user_id column to meal_records table
ALTER TABLE meal_records ADD COLUMN recorded_by_user_id VARCHAR(50);

-- Add foreign key constraint
ALTER TABLE meal_records 
ADD CONSTRAINT fk_meal_records_recorded_by_user 
FOREIGN KEY (recorded_by_user_id) REFERENCES users(id);

-- Add index for better performance
CREATE INDEX idx_meal_records_recorded_by_user ON meal_records(recorded_by_user_id); 