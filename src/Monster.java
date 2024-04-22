import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Monster {
    // Monster Demographics variables
    // Information pulled from https://www.pokemon.com/pokedex
    public int id;
    public String name;
    public String desc;
    public List<String> types = new ArrayList<>();
    public Region region;

    // Monster stats (spec = special)
    public int hp;
    public int attack;
    public int defense;
    public int specAttack;
    public int specDefense;
    public int speed;

    // Files to load (converted from String to path)
    private Path monsterPath = Paths.get("data/singlePokemon.xml");
    final private Path regionPath = Paths.get("data/regions.csv");

    public Monster() {
        loadMonster();
        loadRegion();
    }
    /**
     * LoadMonster is called at start with the default path of data/singlePokemon.xml.
     * File can be overriden and a different file loaded by calling this version of the method.
     * @param newFile The string value of the path and filename for new monster file.
     */
    public void loadMonster(String newFile) {
        monsterPath = Paths.get(newFile);
    }
    private void loadMonster() {
        try {
            // Map to be used to check for if we are in a tag or not
            // when Key is encountered as a field we set it to True
            // Then process that variable until end flag found (move to false)
            Map<String, Boolean> flags = new HashMap<>();
            flags.put("id", false);
            flags.put("name", false);
            flags.put("types", false);
            flags.put("stats", false);

            // counter variable for stats
            short counter = 0;

            for (String line : Files.readAllLines(monsterPath)) {
                // now in for loop we check for a substring (the key)
                // IF the key exists set to true and process that key with next line
                // IF the key is found again - this time the / then move on

                /* TO TEST - substrings used:
                if (line.length()>1) {
                    System.out.println(line.length());
                    System.out.printf("Substrings used: 1/len-1 %s\n",
                            line.substring(1, line.length() - 1));
                }
                */

                // FIRST: check if we have a true value at all (not: skip to tag checking)
                if (flags.containsValue(true)) {
                    if (flags.get("id")) {
                        // If id just get it (one line - reset flag after)
                        id = Integer.parseInt(line);
                        flags.replace("id", false);
                    } else if (flags.get("name")) {
                        // If name just get it (one line - reset flag after)
                        name = line;
                        flags.replace("name", false);
                    } else if (flags.get("types")) {
                        // types are a list of all items until /types is encountered
                        // So check if line is /types - if not add the line or reset flag
                        if (line.substring(1, line.length()-1).equals("/types")) {
                            flags.replace("types", false);
                        } else {
                            types.add(line);
                        }
                    } else if (flags.get("stats")) {
                        // stats are a list of 6 stats - always in the same order
                        // So we'll use a counter (see before for until /stats encountered

                        switch (counter) {
                            // Check counter from 0 to 5 - set a stat
                            // If its not 0-5 then break out by setting flag
                            case 0:
                                hp = Integer.parseInt(line);
                                break;
                            case 1:
                                attack = Integer.parseInt(line);
                                break;
                            case 2:
                                defense = Integer.parseInt(line);
                                break;
                            case 3:
                                specAttack = Integer.parseInt(line);
                                break;
                            case 4:
                                specDefense = Integer.parseInt(line);
                                break;
                            case 5:
                                speed = Integer.parseInt(line);
                                break;
                            default:
                                flags.replace("stats", false);
                        }
                        counter++;
                    }
                } else {
                    // SECOND: if no set flag found: we check if this is a tag and set the flag
                    if (flags.containsKey(line.substring(1,line.length()-1))) {
                        flags.replace(line.substring(1,line.length()-1), true);
                        // This will only hit on <> tags not </tags> because / stops it
                    }
                }

            }
        } catch (IOException ex) {
            System.out.println("Problem with Monster File!");
            ex.printStackTrace();
        }
    }

    /**
     * Load the Kanto Region Information for Monster (first line after header)
     */
    private void loadRegion() {
        // we are just going to assume that Bulbasaur is the first region
        // Typically this would be a seperate table in a database
        // variable to hold all regions
        try {
            // Technically I can make this an assignment and then Split ....but
            String[] kanto = Files.readAllLines(regionPath).get(1).split(",");
            region = new Region(Integer.parseInt(kanto[0]), kanto[1], kanto[2]);
        } catch (IOException ex) {
            System.out.println("Region file is unreadable or not found!");
            ex.printStackTrace();
        }
    }

    /**
     * Returns a string of the monster created
     * @return A string of the important demo fields and stats of monster
     */
    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", types=" + types +
                ", region=" + region +
                ", hp=" + hp +
                ", attack=" + attack +
                ", defense=" + defense +
                ", specAttack=" + specAttack +
                ", specDefense=" + specDefense +
                ", speed=" + speed +
                ", monsterPath=" + monsterPath +
                ", regionPath=" + regionPath +
                '}';
    }

    /**
     * Loads & prints the title screen (monster) from static file.
     */
    public void loadTitle() {
        try {
            Files.lines(Paths.get("static_images/title.txt"))
                    .forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println("Cannot load title: file is unreadable or not found!");
            ex.printStackTrace();
        }
    }

    /**
     * Loads and prints the image file with the same name as the monster.
     */
    public void monsterImage() {
        final String image = String.format("static_images/%s.txt", name);
        try {
            Files.lines(Paths.get(image)).forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println("Cannot load monster image: file is unreadable or not found!");
            ex.printStackTrace();
        }
    }

    public void printToFile(String monsterFile) {
        try {
            Files.write(Paths.get(monsterFile), this.toString().getBytes());
        } catch (IOException ex) {
            System.out.println("File cannot be opened for writing!");
            ex.printStackTrace();
        }
    }
}
