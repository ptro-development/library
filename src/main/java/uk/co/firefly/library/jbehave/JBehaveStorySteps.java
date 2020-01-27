package uk.co.firefly.library.jbehave;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JBehaveStorySteps {
	private Map<String, String> urlEndpoints;
	private Map<String, String> cache;
	private String HOST = "localhost";
	private int PORT = 8080;

	private HttpPut putRequest;
	private HttpPost postRequest;
	private HttpDelete deleteRequest;
	private HttpGet getRequest;
	private CloseableHttpClient httpClient;

	private CloseableHttpResponse httpResponse;
	private String responseString;

	public JBehaveStorySteps() {

		// As apache http client can be quite noisy
		final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("org.apache");
		ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
		logbackLogger.setLevel(ch.qos.logback.classic.Level.INFO);

		urlEndpoints = new HashMap<String, String>();
		urlEndpoints.put("BOOK-RESOURCE", "/library/books");
		urlEndpoints.put("PUBLISHER-RESOURCE", "/library/publishers");
		urlEndpoints.put("AUTHOR-RESOURCE", "/library/authors");
		urlEndpoints.put("PICTURE-RESOURCE", "/library/pictures");

		cache = new HashMap<String, String>();
	}

	public void initClient() {
		if (httpClient == null) {
			httpClient = HttpClients.createDefault();
		}
	}

	public void closeClient() {
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpClient = null;
	}

	@Given("a POST request with the url $url and json $json")
	public void setPostRequest(@Named("url") String url, @Named("$json") String json) throws URISyntaxException {
		JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();
		StringEntity entity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT).setPath(urlEndpoints.get(url)).build();
		postRequest = new HttpPost(uri);
		postRequest.setEntity(entity);
	}

	@Given("a POST request with the url $url and type $type and bookId of $id")
	public void setPicturePostRequest(@Named("url") String url, @Named("type") String type, @Named("id") String id)
			throws URISyntaxException, IOException {
		JsonObject jsonObject = (new JsonParser()).parse(responseString).getAsJsonObject();
		id = jsonObject.get(id).getAsString();
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT).setPath(urlEndpoints.get(url))
				.setParameter("type", type).setParameter("bookId", id).build();
		HttpEntity httpEntity = MultipartEntityBuilder.create()
				.addBinaryBody("file", "picture body".getBytes(), ContentType.create("image/jpeg"), "test.jpg")
				.build();
		postRequest = new HttpPost(uri);
		postRequest.setEntity(httpEntity);
	}

	@Given("a PUT request with an url key $key identifier $id and simple key-value json $jsonString")
	public void putRequest(@Named("url") String url, @Named("id") String id, @Named("jsonString") String jsonString)
			throws URISyntaxException {
		JsonObject previousJson = (new JsonParser()).parse(responseString).getAsJsonObject();
		JsonObject newKeyValues = (new JsonParser()).parse(jsonString).getAsJsonObject();
		for (String key : newKeyValues.keySet()) {
			previousJson.addProperty(key, newKeyValues.get(key).getAsString());
		}
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT)
				.setPath(urlEndpoints.get(url) + "/" + previousJson.get(id).getAsString()).build();
		StringEntity entity = new StringEntity(previousJson.toString(), ContentType.APPLICATION_JSON);
		putRequest = new HttpPut(uri);
		putRequest.setEntity(entity);
	}

	@Given("a previous resource was created at an url key $url with an identifier $id and is ready for DELETE operation")
	public void prepareDelete(@Named("$url") String url, @Named("id") String id) throws URISyntaxException {
		JsonObject jsonObject = (new JsonParser()).parse(responseString).getAsJsonObject();
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT)
				.setPath(urlEndpoints.get(url) + "/" + jsonObject.get("id").getAsString()).build();
		deleteRequest = new HttpDelete(uri);

}

	@Given("a previous resource was created at an url key $url with an identifier $id and is ready for GET operation")
	public void prepareGet(@Named("$url") String url, @Named("id") String id) throws URISyntaxException {
		JsonObject jsonObject = (new JsonParser()).parse(responseString).getAsJsonObject();
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT)
				.setPath(urlEndpoints.get(url) + "/" + jsonObject.get(id).getAsString()).build();
		getRequest = new HttpGet(uri);
	}

	@Given("a previous resources were created at an url key $key and are ready for GET operation")
	public void prepareGetAll(@Named("$url") String url) throws URISyntaxException {
		URI uri = new URIBuilder().setScheme("http").setHost(HOST).setPort(PORT).setPath(urlEndpoints.get(url)).build();
		getRequest = new HttpGet(uri);
	}

	@When("a caller hits POST endpoint")
	public void runPost() throws URISyntaxException, ClientProtocolException, IOException {
		initClient();
		httpResponse = httpClient.execute(postRequest);
		responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		closeClient();
	}

	@When("a caller hits PUT endpoint")
	public void runPut() throws URISyntaxException, ClientProtocolException, IOException {
		initClient();
		httpResponse = httpClient.execute(putRequest);
		responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		closeClient();
	}

	@When("a caller hits DELETE endpoint")
	public void runDelete() throws ClientProtocolException, IOException {
		initClient();
		httpResponse = httpClient.execute(deleteRequest);
		//responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		closeClient();
	}

	@When("a caller hits GET endpoint")
	public void runGet() throws ClientProtocolException, IOException {
		initClient();
		httpResponse = httpClient.execute(getRequest);
		responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		closeClient();
	}

	@Then("an expected response code should be $code")
	public void testResponseCode(@Named("$code") int code) {
		if (code != httpResponse.getStatusLine().getStatusCode()) {
			System.out.println("Status line:" + httpResponse.getStatusLine() + " body:" + responseString);
		}
		Assert.assertEquals(code, httpResponse.getStatusLine().getStatusCode());
	}

	@Then("save last response body under key $key")
	public void saveResponse(@Named("$key") String key) {
		cache.put(key, responseString);
	}

	@Given("a loading of previous response body under key $key was successful")
	public void loadPreviousResponse(@Named("$key") String key) {
		responseString = cache.get(key);
	}

	@Then("a response should be an array where each element have strings $strings")
	public void testResponseKeysInArray(@Named("$strings") String strings) {
		JsonArray jsonArray = (new JsonParser()).parse(responseString).getAsJsonArray();
		boolean found = true;
		for (JsonElement element : jsonArray) {
			if(!findElements(element.getAsJsonObject().toString(), strings.split(","))) {
				found = false;
				System.out.println("Response was:<<" + responseString + ">>");
				break;
			};
		}
		Assert.assertTrue(found);
	}
	
	@Then("a response should have strings $keys")
	public void testResponseKeys(@Named("$keys") String keys) {
		if(!findElements(responseString, keys.split(","))) {
			System.out.println("Response was:<<" + responseString + ">>");
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}
	
	public boolean findElements(String stringToSearch, String[] elements) {
		boolean found = true;
		for (String element : elements) {
			if (stringToSearch.indexOf(element) < 0) {
				found = false;
				System.out.println("String <<" + element + ">> was not found in <<" + stringToSearch + ">>");
				break;
			}
		}
		return found;
	}

}