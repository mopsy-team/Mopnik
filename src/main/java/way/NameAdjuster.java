package way;

import br.zuq.osm.parser.model.OSMNode;

public class NameAdjuster {
    public static String[] getWayNames(OSMNode node) {
        if (!node.getAllTags().containsKey("ref")) {
            return new String[]{};
        }
        String[] refs = node.getAllTags().get("ref").replaceAll("\\s+", "").split(";");
        for (int i = 0; i < refs.length; ++i) {
            String ref = refs[i];
            double lon = Double.parseDouble(node.lon);
            double lat = Double.parseDouble(node.lat);
            switch (ref) {
                case "A1":
                    if (lat < 50.26) {
                        refs[i] = "A1a";
                    } else if (lat < 50.31) {
                        refs[i] = "A1b";
                    } else if (lat < 50.42 && lon < 18.93) {
                        refs[i] = "A1c";
                    } else if (lat < 50.455 && lon < 19.058) {
                        refs[i] = "A1d";
                    }

                    break;
                case "A2":
                    if (lon > 21) {
                        refs[i] = "A2a";
                    }
                    break;
                case "S1":
                    if (lat > 50.4) {
                        refs[i] = "S1b";
                    }
                    break;
                case "S3":
                    if (lat > 53.34) {
                    } else if (lat > 52.22) {
                        refs[i] = "S3a";
                    }
                    break;
                case "S5":
                    if (lat > 52.65) {
                        refs[i] = "S5c";
                    } else if (lat > 52.315 && lon > 17.12) {
                        refs[i] = "S5d";
                    } else if (lat > 51.97) {
                    }
                    break;
                case "S7":  //TODO jest wiecej :(
                    if (lat > 54.142) {
                        refs[i] = "S7i";
                    } else if (lat > 53.6) {
                        if (lon > 19.44) {
                            refs[i] = "S7g";
                        }
                    } else if (lat > 53.12) {
                        refs[i] = "S7j";
                    } else if (lat > 52.59) {
                        refs[i] = "S7d";
                    } else if (lat > 51.8) {
                        refs[i] = "S7";
                    } else if (lat > 51.558) {
                        refs[i] = "S7a";
                    } else if (lat > 51.046) {
                        if (lat < 51.4) {
                            refs[i] = "S7h";
                        }
                    } else if (lat > 50.968) {
                        refs[i] = "S7f";
                    } else if (lat > 50.936) {
                        refs[i] = "S7e";
                    } else if (lat > 50.645) {
                        refs[i] = "S7k";
                    }
                    break;
                case "S8":
                    if (lon > 17 && lon < 19.6) {
                        refs[i] = "S8e";
                    } else if (lon > 20.83 && lon < 21.26) {
                        refs[i] = "S8f";
                    } else if (lon > 21.26 && lon < 21.503) {
                        refs[i] = "S8d";
                    } else if (lon > 21.85 && lon < 23.05) {
                        refs[i] = "S8g";
                    }
                    break;
                case "S10":
                    if (lon < 15.1) {
                        refs[i] = "S10b";
                    } else if (lon < 17.31) {
                        refs[i] = "S10c";
                    } else if (lon < 18.68) {
                        refs[i] = "S10a";
                    }
                    break;
            }
        }
        return refs;
    }
}
