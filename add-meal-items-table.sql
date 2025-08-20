-- Add meal_items table for individual food items under meal categories
-- This allows each meal category to have multiple items like FirFir, Kinche, Salad, Pizza, Burger, etc.

CREATE TABLE meal_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    meal_category_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url TEXT,
    color VARCHAR(50) NOT NULL DEFAULT '#3B82F6',
    total_available INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (meal_category_id) REFERENCES meal_categories(id) ON DELETE CASCADE,
    UNIQUE(meal_category_id, name)
);

-- Create indexes for better performance
CREATE INDEX idx_meal_items_category_id ON meal_items(meal_category_id);
CREATE INDEX idx_meal_items_active ON meal_items(is_active);
CREATE INDEX idx_meal_items_name ON meal_items(name);

-- Add some sample meal items for testing
INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
-- Breakfast Fasting items
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - ጾም' LIMIT 1), 'FirFir', 'Traditional Ethiopian breakfast made from shredded injera', '#FF6B6B', 50, true),
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - ጾም' LIMIT 1), 'Kinche', 'Ethiopian porridge made from cracked wheat', '#4ECDC4', 30, true),
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - ጾም' LIMIT 1), 'Salad', 'Fresh vegetable salad', '#45B7D1', 25, true),

-- Breakfast Non-Fasting items
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - የፍስግ' LIMIT 1), 'FirFir with Egg', 'FirFir served with scrambled eggs', '#FF6B6B', 40, true),
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - የፍስግ' LIMIT 1), 'Kinche with Butter', 'Kinche served with butter', '#4ECDC4', 35, true),
((SELECT id FROM meal_categories WHERE name = 'ቁርስ - የፍስግ' LIMIT 1), 'Pizza', 'Italian pizza with various toppings', '#96CEB4', 20, true),

-- Lunch Fasting items
((SELECT id FROM meal_categories WHERE name = 'ምሳ - ጾም' LIMIT 1), 'Shiro', 'Ground chickpea stew', '#FFEAA7', 60, true),
((SELECT id FROM meal_categories WHERE name = 'ምሳ - ጾም' LIMIT 1), 'Misir Wot', 'Red lentil stew', '#DDA0DD', 45, true),
((SELECT id FROM meal_categories WHERE name = 'ምሳ - ጾም' LIMIT 1), 'Gomen', 'Collard greens', '#98D8C8', 30, true),

-- Lunch Non-Fasting items
((SELECT id FROM meal_categories WHERE name = 'ምሳ - የፍስግ' LIMIT 1), 'Doro Wot', 'Spicy chicken stew', '#F7DC6F', 40, true),
((SELECT id FROM meal_categories WHERE name = 'ምሳ - የፍስግ' LIMIT 1), 'Tibs', 'Sautéed meat and vegetables', '#BB8FCE', 35, true),
((SELECT id FROM meal_categories WHERE name = 'ምሳ - የፍስግ' LIMIT 1), 'Burger', 'Beef burger with fries', '#85C1E9', 25, true),

-- Dinner Fasting items
((SELECT id FROM meal_categories WHERE name = 'ራቁት - ጾም' LIMIT 1), 'Ful', 'Fava bean stew', '#F8C471', 40, true),
((SELECT id FROM meal_categories WHERE name = 'ራቁት - ጾም' LIMIT 1), 'Salata', 'Mixed vegetable salad', '#82E0AA', 30, true),
((SELECT id FROM meal_categories WHERE name = 'ራቁት - ጾም' LIMIT 1), 'Soup', 'Vegetable soup', '#F1948A', 25, true),

-- Dinner Non-Fasting items
((SELECT id FROM meal_categories WHERE name = 'ራቁት - የፍስግ' LIMIT 1), 'Kitfo', 'Ethiopian steak tartare', '#C39BD3', 30, true),
((SELECT id FROM meal_categories WHERE name = 'ራቁት - የፍስግ' LIMIT 1), 'Dulet', 'Minced meat and organs', '#F7DC6F', 25, true),
((SELECT id FROM meal_categories WHERE name = 'ራቁት - የፍስግ' LIMIT 1), 'Pasta', 'Italian pasta with sauce', '#85C1E9', 20, true); 