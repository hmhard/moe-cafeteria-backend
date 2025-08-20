-- Add allowed_count column to meal_categories table
-- This specifies how many items an employee can select from this category

ALTER TABLE meal_categories 
ADD COLUMN allowed_count INTEGER NOT NULL DEFAULT 1;

-- Update existing categories with reasonable default values
UPDATE meal_categories 
SET allowed_count = 2 
WHERE name LIKE '%ምሳ%' OR name LIKE '%Lunch%';

UPDATE meal_categories 
SET allowed_count = 1 
WHERE name LIKE '%ቁርስ%' OR name LIKE '%Breakfast%';

UPDATE meal_categories 
SET allowed_count = 1 
WHERE name LIKE '%ራቁት%' OR name LIKE '%Dinner%'; 