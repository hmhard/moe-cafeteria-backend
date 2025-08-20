-- Add order_number column to meal_records table
ALTER TABLE meal_records ADD COLUMN order_number VARCHAR(50) UNIQUE;

-- Create index for better performance
CREATE INDEX idx_meal_records_order_number ON meal_records(order_number);

-- Update existing records with order numbers (if any exist)
-- This will generate order numbers for existing records
UPDATE meal_records 
SET order_number = CONCAT('ORD-', DATE_FORMAT(recorded_at, '%Y%m%d'), '-', LPAD(id % 10000, 4, '0'))
WHERE order_number IS NULL; 