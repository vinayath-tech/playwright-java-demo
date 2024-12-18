package pages;

import com.microsoft.playwright.Page;

import javax.sql.rowset.CachedRowSet;
import java.util.List;

public class CheckOut {

    private final Page page;

    public CheckOut(Page page) {
        this.page = page;
    }

    public record CartLineItem(String title, int qty, double price, double total){}

    public List<CartLineItem> getLineItems() {

        page.locator("app-cart tbody tr").waitFor();
        return page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(
                        row -> {
                            String itemName = row.getByTestId("product-title").innerText();
                            int qty = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                            double linePrice = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                            return new CartLineItem(itemName, qty, price, linePrice);
                        }
                ).toList();
    }

    public String price(String value) {
        return value.replace("$"," ");
    }
}
