package com.dimaria95.testapp;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {
	public HomePage homePage;
	public ProductPage prodPage;
	public CartPage cartPage;
	public CheckoutPage checkPage;
	public ShippingPage shippingPage;

	@BeforeMethod
	public void initEmptyCart() {

		// go to home page
		homePage = new HomePage(driver);

		// go to cart and check if cart title is shown
		cartPage = homePage.goToCart();
		Assert.assertTrue(cartPage.checkCartTitle());

		// if cart is not empty -> remove all items from cart and check if cart is now
		// empty
		if (cartPage.getCounter() != 0) {
			cartPage.removeAllItems();
		}
		Assert.assertEquals(cartPage.getCounter(), 0);

		// go back to home page
		homePage = cartPage.goHome();
	}

	@Test
	public void checkoutFlowTest() {
		System.out.println("START checkoutFlowTest TEST");

		// Select item no 2 and go on product page
		prodPage = homePage.clickOnProduct(1);
		Assert.assertEquals(prodPage.getAddToCart(), "add to cart");

		// Get price from product page and add product to the cart
		double itemPrice = prodPage.addToCart();

		// Go to cart
		cartPage = prodPage.goToCart();
		Assert.assertTrue(cartPage.checkCartTitle());

		// Get price from cart and compare
		double cartPrice = cartPage.getTotalPrice();
		Assert.assertEquals(itemPrice, cartPrice);

		// Get number of items in cart from navigation bar
		int numOfItems = cartPage.getCounter();
		Assert.assertEquals(numOfItems, 1);
		
		// click on checkout button and see if contact is displayed correctly
		checkPage = cartPage.goToCheckout();
		Assert.assertTrue(checkPage.isContactCorrect());

		// check if the prices from the cart and checkout page are the same
		double checkoutPrice = checkPage.getCheckoutPrice();
		Assert.assertEquals(itemPrice, checkoutPrice);

		// proceed to shipping page and check if title,email,and payment are available
		shippingPage = checkPage.goToShippingGuest("dimic.nikola@gmail.com", "Nikola", "Dimic", "Street 123", "London",
				"GX11 1AA");
		Assert.assertTrue(shippingPage.checkPageTitle());
		Assert.assertTrue(shippingPage.checkEmail("dimic.nikola@gmail.com"));
		Assert.assertTrue(shippingPage.checkPaymentAvailability());

		// Go back to main page 
		homePage = shippingPage.goHome();
		Assert.assertTrue(homePage.isUrlCorrect());

	}

}