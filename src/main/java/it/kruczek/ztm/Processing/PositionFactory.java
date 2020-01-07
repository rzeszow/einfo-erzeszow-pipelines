package it.kruczek.ztm.Processing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PositionFactory {

    public JSONObject create(String filePath, Node node) {
        JSONArray givenPosition = new JSONArray(node.getTextContent());
        JSONObject position = new JSONObject();

        Timestamp timestamp = getTimestamp(filePath);
        position.put("@timestamp", timestamp.getTime());
        position.put("TimestampAt", timestamp.getTime() / 1000L);
        position.put("RadioNo", Integer.parseInt(givenPosition.get(0).toString().trim()));
        position.put("BusNo", Integer.parseInt(givenPosition.get(1).toString().trim()));
        position.put("LineNo", givenPosition.get(2).toString().trim());
        position.put("Route", givenPosition.get(3).toString().trim());
        position.put("Direction", givenPosition.get(4).toString().trim());
        position.put("ServiceNo", givenPosition.get(5).toString().trim());

        position.put("BusStops", Integer.parseInt(givenPosition.get(6).toString().trim()));
        position.put("DistanceToNextBusStop", Integer.parseInt(givenPosition.get(7).toString().trim()));
        position.put("RemainingToNextBusStop", Integer.parseInt(givenPosition.get(8).toString().trim()));
        position.put("GeoPointPosition", this.createGeoPointPositionObject(
                Float.parseFloat(givenPosition.get(10).toString().trim()),
                Float.parseFloat(givenPosition.get(9).toString().trim())
        ));

        position.put("Position", this.createPositionObject(
                Float.parseFloat(givenPosition.get(10).toString().trim()),
                Float.parseFloat(givenPosition.get(9).toString().trim())
        ));
        position.put("PrevPosition", this.createPositionObject(
                Float.parseFloat(givenPosition.get(12).toString().trim()),
                Float.parseFloat(givenPosition.get(11).toString().trim())
        ));

        position.put("VariationInSeconds", Integer.parseInt(givenPosition.get(13).toString().trim()));
        position.put("VariationAsFormattedText", givenPosition.get(14).toString().trim());
        position.put("State", Integer.parseInt(givenPosition.get(15).toString().trim()));
        position.put("TimeDeparture", givenPosition.get(16).toString().trim());

        position.put("NextServiceNo", Integer.parseInt(givenPosition.get(17).toString().trim()));

        position.put("NextTimeDeparture", givenPosition.get(18).toString().trim());
        position.put("NextLineNo", givenPosition.get(19).toString().trim());
        position.put("NextRoute", givenPosition.get(20).toString().trim());
        position.put("NextDirection", givenPosition.get(21).toString().trim());

        position.put("SecondsToStart", Integer.parseInt(givenPosition.get(22).toString().trim()));

        position.put("TypeOfVehicle", givenPosition.get(23).toString().trim());
        position.put("Attributes", givenPosition.get(24).toString().trim());

        return position;
    }

    private JSONObject createPositionObject(Float latitude, Float longitude) {
        JSONObject geoPoint = new JSONObject();
        geoPoint.put("type", "Point");
        geoPoint.put("coordinates", new JSONArray(new ArrayList<Float>() {
            {
                add(latitude);
                add(longitude);
            }
        }));

        return geoPoint;
    }

    private JSONObject createGeoPointPositionObject(Float latitude, Float longitude) {
        JSONObject geoPoint = new JSONObject();
        geoPoint.put("lat", latitude);
        geoPoint.put("lon", longitude);

        return geoPoint;
    }

    private static Timestamp getTimestamp(String filePath) {
        Path p = Paths.get(filePath);
        String basename = p.getFileName().toString().replaceFirst("[.][^.]+$", "");

        return new Timestamp(Long.valueOf(basename) * 1000L);
    }
}
