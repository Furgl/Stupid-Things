package furgl.stupidThings.common.recipe;

import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

public class ShapedMatchingRecipe extends ShapedRecipes 
{
	//the ItemStack that you get when you craft the recipe
	private ItemStack recipeOutput;

	public ShapedMatchingRecipe(int width, int height, ItemStack[] input, ItemStack output) {
		super(width, height, input, output);
		this.recipeOutput = output;
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
