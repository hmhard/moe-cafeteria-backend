# Meal Items System Documentation

## Overview

The meal items system allows tracking individual food items (like FirFir, Kinche, Shiro, etc.) that are selected for each meal record. This provides detailed insights into what specific items employees choose for their meals.

## Database Schema

### Tables

#### 1. `meal_items` Table
Stores individual food items available under each meal category.

```sql
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
```

#### 2. `meal_record_items` Table
Stores the specific items selected for each meal record.

```sql
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
```

## Sample Data

The system includes sample meal items for each category:

### Breakfast Fasting (ቁርስ - ጾም)
- FirFir (Traditional Ethiopian breakfast)
- Kinche (Ethiopian porridge)
- Salad (Fresh vegetable salad)

### Breakfast Non-Fasting (ቁርስ - የፍስግ)
- FirFir with Egg
- Kinche with Butter
- Pizza

### Lunch Fasting (ምሳ - ጾም)
- Shiro (Ground chickpea stew)
- Misir Wot (Red lentil stew)
- Gomen (Collard greens)

### Lunch Non-Fasting (ምሳ - የፍስግ)
- Doro Wot (Spicy chicken stew)
- Tibs (Sautéed meat and vegetables)
- Burger (Beef burger with fries)

### Dinner Fasting (ራቁት - ጾም)
- Ful (Fava bean stew)
- Salata (Mixed vegetable salad)
- Soup (Vegetable soup)

### Dinner Non-Fasting (ራቁት - የፍስግ)
- Kitfo (Ethiopian steak tartare)
- Dulet (Minced meat and organs)
- Pasta (Italian pasta with sauce)

## API Endpoints

### 1. Record Meal with Items
```
POST /meal-records/record-with-items
```

**Request Body:**
```json
{
  "cardId": "CARD123",
  "mealCategoryId": "uuid",
  "selectedItems": [
    {
      "mealItemId": "uuid",
      "quantity": 2
    },
    {
      "mealItemId": "uuid",
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": "meal-record-uuid",
  "employeeId": "EMP001",
  "cardId": "CARD123",
  "mealTypeId": "breakfast",
  "mealCategoryId": "uuid",
  "mealName": "ቁርስ - ጾም",
  "category": "fasting",
  "priceType": "supported",
  "normalPrice": 30.00,
  "supportedPrice": 20.00,
  "actualPrice": 20.00,
  "supportAmount": 10.00,
  "timestamp": "2024-01-15T08:30:00",
  "orderNumber": "ORD-20240115-1234",
  "mealItems": [
    {
      "id": "item-uuid",
      "mealRecordId": "meal-record-uuid",
      "mealItemId": "meal-item-uuid",
      "mealItemName": "FirFir",
      "quantity": 2,
      "pricePerItem": 20.00,
      "totalPrice": 40.00,
      "createdAt": "2024-01-15T08:30:00"
    }
  ]
}
```

### 2. Get Meal Record with Items
```
GET /meal-records/{id}
```

Returns meal record with associated meal items.

## Database Views and Functions

### 1. `meal_records_with_items` View
Provides a comprehensive view of meal records with their associated items.

```sql
SELECT * FROM meal_records_with_items;
```

### 2. `get_meal_record_summary()` Function
Returns a summary of a meal record with item details.

```sql
SELECT * FROM get_meal_record_summary('meal-record-uuid');
```

## Frontend Integration

### 1. Meal Item Selection
The scan page allows operators to select specific meal items before recording a meal.

### 2. Receipt Printing
Receipts now include the selected meal items with quantities and prices.

### 3. Admin Records View
The admin records page displays meal items for each record in a dedicated column.

## Setup Instructions

### 1. Run Database Setup
```bash
psql -h localhost -U postgres -d cafeteria -f setup-meal-items-system.sql
```

### 2. Test the System
```bash
psql -h localhost -U postgres -d cafeteria -f test-meal-items-system.sql
```

### 3. Restart Backend
```bash
cd cateteria-backend
./gradlew bootRun
```

## Usage Workflow

1. **Operator selects meal category** (e.g., Breakfast Fasting)
2. **Available items are displayed** (FirFir, Kinche, Salad)
3. **Operator selects items and quantities**
4. **Employee scans card**
5. **System records meal with selected items**
6. **Receipt is printed with item details**
7. **Admin can view item details in records**

## Benefits

1. **Detailed Tracking**: Know exactly what items employees choose
2. **Inventory Management**: Track item popularity and availability
3. **Cost Analysis**: Understand item-level pricing and costs
4. **Customer Preferences**: Analyze food preferences by department/employee
5. **Receipt Accuracy**: Detailed receipts with item breakdown

## Maintenance

### Adding New Meal Items
```sql
INSERT INTO meal_items (meal_category_id, name, description, color, total_available, is_active)
VALUES ('category-uuid', 'New Item', 'Description', '#FF0000', 50, true);
```

### Updating Item Availability
```sql
UPDATE meal_items 
SET total_available = 30 
WHERE name = 'FirFir' AND meal_category_id = 'category-uuid';
```

### Deactivating Items
```sql
UPDATE meal_items 
SET is_active = false 
WHERE name = 'Old Item';
```

## Troubleshooting

### Common Issues

1. **Items not showing**: Check if `is_active = true`
2. **Foreign key errors**: Ensure meal categories exist
3. **Duplicate items**: Check unique constraint on (meal_category_id, name)

### Useful Queries

```sql
-- Check items by category
SELECT mc.name as category, mi.name as item, mi.total_available
FROM meal_categories mc
JOIN meal_items mi ON mc.id = mi.meal_category_id
WHERE mi.is_active = true
ORDER BY mc.name, mi.name;

-- Check meal records with items
SELECT mr.order_number, mr.meal_name, COUNT(mri.id) as items_count
FROM meal_records mr
LEFT JOIN meal_record_items mri ON mr.id = mri.meal_record_id
GROUP BY mr.id, mr.order_number, mr.meal_name
ORDER BY mr.recorded_at DESC;
``` 