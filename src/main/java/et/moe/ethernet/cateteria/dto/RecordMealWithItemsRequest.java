package et.moe.ethernet.cateteria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordMealWithItemsRequest {
    
    private String cardId;
    private String mealCategoryId;
    private List<SelectedMealItem> selectedItems;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedMealItem {
        private String mealItemId;
        private Integer quantity;
    }
} 