package PoolGame.config;

import PoolGame.objects.*;
import PoolGame.GameManager;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** Reads in pocket section of JSON. */
public class PocketReader implements Reader {

    public void parse(String path, GameManager gameManager) {
        JSONParser parser = new JSONParser();
        ArrayList<Pocket> pockets = new ArrayList<Pocket>();

        try {
            Object object = parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // reading the "Table" section:
            JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

            // reading the "Table: pockets" array:
            JSONArray jsonPockets = (JSONArray) jsonTable.get("pockets");

            // reading from the section
            for (Object obj: jsonPockets) {
                JSONObject jsonPocket = (JSONObject) obj;

                // the pocket position, radius are doubles
                Double positionX = (Double) ((JSONObject) jsonPocket.get("position")).get("x");
                Double positionY = (Double) ((JSONObject) jsonPocket.get("position")).get("y");
                Double radius = (Double) jsonPocket.get("radius");

                // Check pocket is within bounds
                Table table = gameManager.getTable();
                if (positionX > table.getxLength() || positionX < 0 || positionY > table.getyLength() || positionY < 0) {
                    System.out.println("Pocket position is outside the table");
                    System.exit(0);
                }

                // Builder code
                PoolPocketBuilder builder = new PoolPocketBuilder();
                builder.setxPos(positionX);
                builder.setyPos(positionY);
                builder.setRadius(radius);
                pockets.add(builder.build());
            }

            gameManager.getTable().setPockets(pockets);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
