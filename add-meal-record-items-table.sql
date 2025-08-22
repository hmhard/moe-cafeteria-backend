-- Add meal_record_items table to store individual meal items selected for each meal record
-- This allows tracking which specific items (FirFir, Kinche, etc.) were selected for each meal

CREATE TABLE meal_record_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    meal_record_id UUID NOT NULL,
    meal_item_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    price_per_item DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (meal_record_id) REFERENCES meal_records(id) ON DELETE CASCADE,
    FOREIGN KEY (meal_item_id) REFERENCES meal_items(id) ON DELETE RESTRICT,
    UNIQUE(meal_record_id, meal_item_id)
);

-- Create indexes for better performance
CREATE INDEX idx_meal_record_items_meal_record_id ON meal_record_items(meal_record_id);
CREATE INDEX idx_meal_record_items_meal_item_id ON meal_record_items(meal_item_id);
CREATE INDEX idx_meal_record_items_created_at ON meal_record_items(created_at); 