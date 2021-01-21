package company;
import simple.JSONObject;
import simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ConnectAndGet {
    public JSONObject returnObj(URL url) {
        try{

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            //if(url.equals(new URL("https://fantasy.espn.com/apis/v3/games/fhl/seasons/2020/segments/0/leagues/56450384?view=mRoster")) || url.equals(new URL("https://fantasy.espn.com/apis/v3/games/fhl/seasons/2020/segments/0/leagues/56450384?view=kona_playercard")))
                //conn.setRequestProperty("Cookie", "SWID ={978AA4BC-0637-4225-BFEA-F97D7BB20AC4};espn_s2 =AECi3dKJt0%2BRxTjIo%2FHhehvdfBwIwqdTT%2BbOSBz6bOLlCpzi584OvXoShe2jxKXTLoOlLYM9DlKSVXpoE5%2FAhnA%2F0PdAw5Z6BXAEq67arXkv39B%2BrEdaPy8eHqOW6uuLhAl6kpFBffWKBxR2L0DPfZ1VEKGH8n7gl62ELJI0Dtm51AtxQaaRKORlgfTvG3uzk%2BhBrVmYeJd70cjRWkb%2BwYzp0RT3GOoDlhOLQjWDEFaRP6HPlebgtLFs4%2FPwahFUUaZcUaEMvlm3IH8KUeKuCVxPjYE8ZTpact0lboVmk3DrUQ%3D%3D");
            conn.connect();
            //SWID :"{978AA4BC-0637-4225-BFEA-F97D7BB20AC4}"
            //espn_s2 :"AECi3dKJt0%2BRxTjIo%2FHhehvdfBwIwqdTT%2BbOSBz6bOLlCpzi584OvXoShe2jxKXTLoOlLYM9DlKSVXpoE5%2FAhnA%2F0PdAw5Z6BXAEq67arXkv39B%2BrEdaPy8eHqOW6uuLhAl6kpFBffWKBxR2L0DPfZ1VEKGH8n7gl62ELJI0Dtm51AtxQaaRKORlgfTvG3uzk%2BhBrVmYeJd70cjRWkb%2BwYzp0RT3GOoDlhOLQjWDEFaRP6HPlebgtLFs4%2FPwahFUUaZcUaEMvlm3IH8KUeKuCVxPjYE8ZTpact0lboVmk3DrUQ%3D%3D"


            //Getting the response code
            int responseCode = conn.getResponseCode();

            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode : " + responseCode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string intp a json object
                JSONParser parse = new JSONParser();
                JSONObject dataObj = (JSONObject) parse.parse(inline);

                return dataObj;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
