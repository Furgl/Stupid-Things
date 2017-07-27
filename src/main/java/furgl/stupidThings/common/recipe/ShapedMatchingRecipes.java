package furgl.stupidThings.common.recipe;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;

public class ShapedMatchingRecipes extends ShapedRecipes
{
	private ItemStack recipeOutput;

	public ShapedMatchingRecipes(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(group, width, height, ingredients, result);
		this.recipeOutput = result;
	}

	@Override
	@Nullable
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack result = this.recipeOutput.copy();
		for (int i = 0; i < inv.getHeight(); ++i)
			for (int j = 0; j < inv.getWidth(); ++j) {
				ItemStack stack = inv.getStackInRowAndColumn(j, i);
				if (stack != null && stack.getItem() instanceof ItemArmor) {
					result.setTagCompound(stack.getTagCompound());
					result.setItemDamage(stack.getItemDamage());
				}
			}
		return result;
	}

}