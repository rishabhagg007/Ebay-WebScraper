package BAX442DDR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EbayWebScraper {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		try {
			
		    
				// Looping through first 10 pages on ebay for items under Auction.
				for (int i = 1; i <= 10; i++) {
					
					 /*
		             * connects to the server and download its HTML page
		             */

					Document doc = Jsoup.connect(
							"https://www.ebay.com/sch/i.html?_from=R40&_nkw=lg+phone&_sacat=0&LH_Auction=1&_pgn=" + i)
							.userAgent("Mozilla/5.0").get();

					// Calling File writer Function
					saveString(new File("ebay_lg_phone_" + i + ".htm"), doc.html(), false);
					Thread.sleep(10_000);
				}
				
			    System.out.println("List of Auction Items for 'Lg Phone' on Ebay - First 10 pages :");
				for (int j = 1; j <= 10; j++) {

					/*
					 *  Calling File reader function which reads all the downloaded HTML pages in
					 *  previous part
					 */					
					String html = loadString(new File("ebay_lg_phone_" + j + ".htm"));

					// Parsing the Html read to Jsoup object
					Document page = Jsoup.parse(html);

					/*
					 * Selects the list tag <li> which has class
					 * "s-item__pl-on-bottom.s-item--watch-at-corner" to get the list of all auction
					 * items for each page.
					 */
					Elements lis = page.select("li.s-item.s-item__pl-on-bottom.s-item--watch-at-corner");
					
					
					System.out.println("\n*********** Page number :  " + j + " ****************");
					
                    int n=1;
					for (Element li : lis) {

						/*
						 * Prints the Product Name, Item price, Number of bids for each item along with
						 * URL for each product. Selects the span tag <span> which has class
						 * "span.s-item__bids.s-item__bidCount" and prints the test inside it
						 */
						System.out.println(n+"."+ " Product Name : "+ li.select("div.s-item__subtitle").text() + "\nItem Price :  "+ li.select("span.s-item__price").text()
								+", Number of Bids for Item : " + li.select("span.s-item__bids.s-item__bidCount").text()
										+ ", URL : " + li.select("a.s-item__link").attr("href") +"\n"  );
						n=n+1;

					}

				}

		} catch (IOException ex) {

			System.out.println("Problem with the connection...");

		}

	}

	public static String loadString(File f) throws IOException {
		byte[] encoded = Files.readAllBytes(f.toPath());
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public static boolean saveString(File f, String s, boolean append) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(f, append);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);) {
			bw.write(s);
			bw.flush();
			return true;
		}
	}

}
