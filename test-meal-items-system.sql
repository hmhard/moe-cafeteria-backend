-- Test script for meal items system
-- This script tests the functionality of the meal items and meal record items tables

-- Test 1: Check if tables exist
SELECT 
    table_name,
    CASE 
        WHEN table_name IS NOT NULL THEN '✓ EXISTS'
        ELSE '✗ MISSING'
    END as status
FROM information_schema.tables 
WHERE table_name IN ('meal_items', 'meal_record_items')
AND table_schema = 'public';

-- Test 2: Check meal items data
SELECT 
    'meal_items' as table_name,
    COUNT(*) as total_items,
    COUNT(DISTINCT meal_category_id) as categories_with_items
FROM meal_items
WHERE is_active = true;

-- Test 3: Show sample meal items by category
SELECT 
    mc.name as category_name,
    mi.name as item_name,
    mi.total_available,
    mi.color
FROM meal_categories mc
JOIN meal_items mi ON mc.id = mi.meal_category_id
WHERE mi.is_active = true
ORDER BY mc.name, mi.name;

-- Test 4: Check meal record items (should be empty initially)
SELECT 
    'meal_record_items' as table_name,
    COUNT(*) as total_records
FROM meal_record_items;

-- Test 5: Test the view
SELECT 
    'meal_records_with_items view' as test_name,
    COUNT(*) as total_rows
FROM meal_records_with_items;

-- Test 6: Show recent meal records (if any exist)
SELECT 
    mr.id,
    mr.employee_id,
    mr.meal_name,
    mr.order_number,
    mr.recorded_at,
    COUNT(mri.id) as items_count
FROM meal_records mr
LEFT JOIN meal_record_items mri ON mr.id = mri.meal_record_id
GROUP BY mr.id, mr.employee_id, mr.meal_name, mr.order_number, mr.recorded_at
ORDER BY mr.recorded_at DESC
LIMIT 5;

-- Test 7: Test the summary function (if meal records exist)
SELECT 
    'get_meal_record_summary function' as test_name,
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM meal_records LIMIT 1
        ) THEN '✓ Can be tested with existing records'
        ELSE '⚠ No meal records to test with'
    END as status;

-- Test 8: Show database constraints
SELECT 
    tc.table_name,
    tc.constraint_name,
    tc.constraint_type,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu 
    ON tc.constraint_name = kcu.constraint_name
LEFT JOIN information_schema.constraint_column_usage ccu 
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.table_name IN ('meal_items', 'meal_record_items')
AND tc.table_schema = 'public'
ORDER BY tc.table_name, tc.constraint_type;

-- Test 9: Show indexes
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE tablename IN ('meal_items', 'meal_record_items')
AND schemaname = 'public'
ORDER BY tablename, indexname; 