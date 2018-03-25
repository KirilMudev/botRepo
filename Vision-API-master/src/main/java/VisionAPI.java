import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VisionAPI {

    private static String theFood;
    private static String fileName;

    public VisionAPI(String fileName) {
        setFileName(fileName);
    }

    public static void main(String... args) throws Exception {
        // Instantiates a client
        String GOOGLE_APPLICATION_CREDENTIALS = "D:\\Downloads\\Health-Vision-API-04782a937fae.json";
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate

            // Reads the image file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }
                 theFood = printResult(res);
                System.out.println(theFood);
            }
        }

      //  connectToBot101();
    }

    private static void connectToBot101() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pizza", "paper");
            HttpPost request = new HttpPost("https://cryptic-scrubland-97529.herokuapp.com/random");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            org.apache.http.HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            com.google.gson.Gson gson = new com.google.gson.Gson();
            Response response = gson.fromJson(json, Response.class);

            System.out.println(response.getExample());
            System.out.println(response.getFr());


            //handle response here...
        } catch (Exception ex) {

            //handle exception here

        }
    }

    private static String printResult(AnnotateImageResponse res) {
        Queue<String> options = new LinkedList<>();
        for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
            annotation.getAllFields().forEach((k, v) -> {
                if (k.toString().equals("google.cloud.vision.v1.EntityAnnotation.description")) {
                    if (isValid(v)) {
                        options.add(v.toString());
                    }
                }
                System.out.printf("%s : %s\n", k, v.toString());
            });
        }
        return options.poll();
    }

    public static void setFileName(String fileName) {
        VisionAPI.fileName = fileName;
    }

    public String getTheFood() {
        return theFood;
    }

    public void setTheFood(String theFood) {
        this.theFood = theFood;
    }

    private static boolean isValid(Object v) {
        return !v.toString().equals("meal")
                && !v.toString().equals("cuisine")
                && !v.toString().equals("dish")
                && !v.toString().equals("food");
    }

    public class Response{

        private String example;
        private String fr;

        public String getExample() {
            return example;
        }
        public void setExample(String example) {
            this.example = example;
        }
        public String getFr() {
            return fr;
        }
        public void setFr(String fr) {
            this.fr = fr;
        }
    }

}
