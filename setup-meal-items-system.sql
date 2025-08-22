-- Comprehensive setup for meal items system
-- This script creates the meal_items and meal_record_items tables with sample data

-- Step 1: Create meal_items table
CREATE TABLE IF NOT EXISTS meal_items (
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

-- Create indexes for meal_items
CREATE INDEX IF NOT EXISTS idx_meal_items_category_id ON meal_items(meal_category_id);
CREATE INDEX IF NOT EXISTS idx_meal_items_active ON meal_items(is_active);
CREATE INDEX IF NOT EXISTS idx_meal_items_name ON meal_items(name);

-- Step 2: Create meal_record_items table
CREATE TABLE IF NOT EXISTS meal_record_items (
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

-- Create indexes for meal_record_items
CREATE INDEX IF NOT EXISTS idx_meal_record_items_meal_record_id ON meal_record_items(meal_record_id);
CREATE INDEX IF NOT EXISTS idx_meal_record_items_meal_item_id ON meal_record_items(meal_item_id);
CREATE INDEX IF NOT EXISTS idx_meal_record_items_created_at ON meal_record_items(created_at);

-- Step 3: Insert sample meal items (only if they don't exist)
DO $$
DECLARE
    breakfast_fasting_id UUID;
    breakfast_non_fasting_id UUID;
    lunch_fasting_id UUID;
    lunch_non_fasting_id UUID;
    dinner_fasting_id UUID;
    dinner_non_fasting_id UUID;
BEGIN
    -- Get meal category IDs
    SELECT id INTO breakfast_fasting_id FROM meal_categories WHERE name = 'ቁርስ - ጾም' LIMIT 1;
    SELECT id INTO breakfast_non_fasting_id FROM meal_categories WHERE name = 'ቁርስ - የፍስግ' LIMIT 1;
    SELECT id INTO lunch_fasting_id FROM meal_categories WHERE name = 'ምሳ - ጾም' LIMIT 1;
    SELECT id INTO lunch_non_fasting_id FROM meal_categories WHERE name = 'ምሳ - የፍስግ' LIMIT 1;
    SELECT id INTO dinner_fasting_id FROM meal_categories WHERE name = 'ራቁት - ጾም' LIMIT 1;
    SELECT id INTO dinner_non_fasting_id FROM meal_categories WHERE name = 'ራቁት - የፍስግ' LIMIT 1;

    -- Insert breakfast fasting items
    IF breakfast_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (breakfast_fasting_id, 'FirFir', 'Traditional Ethiopian breakfast made from shredded injera', '#FF6B6B', 50, true),
        (breakfast_fasting_id, 'Kinche', 'Ethiopian porridge made from cracked wheat', '#4ECDC4', 30, true),
        (breakfast_fasting_id, 'Salad', 'Fresh vegetable salad', '#45B7D1', 25, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;

    -- Insert breakfast non-fasting items
    IF breakfast_non_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (breakfast_non_fasting_id, 'FirFir with Egg', 'FirFir served with scrambled eggs', '#FF6B6B', 40, true),
        (breakfast_non_fasting_id, 'Kinche with Butter', 'Kinche served with butter', '#4ECDC4', 35, true),
        (breakfast_non_fasting_id, 'Pizza', 'Italian pizza with various toppings', '#96CEB4', 20, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;

    -- Insert lunch fasting items
    IF lunch_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (lunch_fasting_id, 'Shiro', 'Ground chickpea stew', '#FFEAA7', 60, true),
        (lunch_fasting_id, 'Misir Wot', 'Red lentil stew', '#DDA0DD', 45, true),
        (lunch_fasting_id, 'Gomen', 'Collard greens', '#98D8C8', 30, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;

    -- Insert lunch non-fasting items
    IF lunch_non_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (lunch_non_fasting_id, 'Doro Wot', 'Spicy chicken stew', '#F7DC6F', 40, true),
        (lunch_non_fasting_id, 'Tibs', 'Sautéed meat and vegetables', '#BB8FCE', 35, true),
        (lunch_non_fasting_id, 'Burger', 'Beef burger with fries', '#85C1E9', 25, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;

    -- Insert dinner fasting items
    IF dinner_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (dinner_fasting_id, 'Ful', 'Fava bean stew', '#F8C471', 40, true),
        (dinner_fasting_id, 'Salata', 'Mixed vegetable salad', '#82E0AA', 30, true),
        (dinner_fasting_id, 'Soup', 'Vegetable soup', '#F1948A', 25, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;

    -- Insert dinner non-fasting items
    IF dinner_non_fasting_id IS NOT NULL THEN
        INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active) VALUES
        (dinner_non_fasting_id, 'Kitfo', 'Ethiopian steak tartare', '#C39BD3', 30, true),
        (dinner_non_fasting_id, 'Dulet', 'Minced meat and organs', '#F7DC6F', 25, true),
        (dinner_non_fasting_id, 'Pasta', 'Italian pasta with sauce', '#85C1E9', 20, true)
        ON CONFLICT (meal_category_id, name) DO NOTHING;
    END IF;
END $$;

-- Step 4: Create a view to easily query meal records with their items
CREATE OR REPLACE VIEW meal_records_with_items AS
SELECT 
    mr.id as meal_record_id,
    mr.employee_id,
    mr.card_id,
    mr.meal_type_id,
    mr.meal_category_id,
    mr.meal_name,
    mr.category,
    mr.price_type,
    mr.normal_price,
    mr.supported_price,
    mr.actual_price,
    mr.support_amount,
    mr.recorded_at,
    mr.order_number,
    mri.id as meal_record_item_id,
    mri.meal_item_id,
    mi.name as meal_item_name,
    mri.quantity,
    mri.price_per_item,
    mri.total_price as item_total_price,
    mri.created_at as item_created_at
FROM meal_records mr
LEFT JOIN meal_record_items mri ON mr.id = mri.meal_record_id
LEFT JOIN meal_items mi ON mri.meal_item_id = mi.id
ORDER BY mr.recorded_at DESC, mri.created_at ASC;

-- Step 5: Create a function to get meal record summary with items
CREATE OR REPLACE FUNCTION get_meal_record_summary(record_id UUID)
RETURNS TABLE (
    meal_record_id UUID,
    employee_id VARCHAR,
    meal_name VARCHAR,
    category VARCHAR,
    actual_price DECIMAL,
    order_number VARCHAR,
    recorded_at TIMESTAMP,
    total_items INTEGER,
    items_list TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        mr.id,
        mr.employee_id,
        mr.meal_name,
        mr.category,
        mr.actual_price,
        mr.order_number,
        mr.recorded_at,
        COALESCE(item_counts.total_items, 0) as total_items,
        COALESCE(item_counts.items_list, 'No items selected') as items_list
    FROM meal_records mr
    LEFT JOIN (
        SELECT 
            mri.meal_record_id,
            COUNT(*) as total_items,
            STRING_AGG(mi.name || ' x' || mri.quantity, ', ' ORDER BY mi.name) as items_list
        FROM meal_record_items mri
        JOIN meal_items mi ON mri.meal_item_id = mi.id
        GROUP BY mri.meal_record_id
    ) item_counts ON mr.id = item_counts.meal_record_id
    WHERE mr.id = record_id;
END;
$$ LANGUAGE plpgsql;

-- Step 6: Display summary of what was created
SELECT 'Meal Items System Setup Complete' as status;

SELECT 
    'meal_items' as table_name,
    COUNT(*) as record_count
FROM meal_items
UNION ALL
SELECT 
    'meal_record_items' as table_name,
    COUNT(*) as record_count
FROM meal_record_items;

SELECT 
    mc.name as category_name,
    COUNT(mi.id) as item_count
FROM meal_categories mc
LEFT JOIN meal_items mi ON mc.id = mi.meal_category_id
GROUP BY mc.id, mc.name
ORDER BY mc.name; 