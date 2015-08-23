package juanmf.grandt.chromosome;

import com.oracle.jrockit.jfr.DataType;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import juanmf.ga.structure.Gen;
import net.sf.jsefa.Deserializer;
import net.sf.jsefa.csv.CsvIOFactory;
import net.sf.jsefa.csv.annotation.CsvDataType;
import net.sf.jsefa.csv.annotation.CsvField;
import org.apache.commons.collections.list.SetUniqueList;

/**
 * A player of some real team, that can be used in a GranDT team.
 * 
 * Expected cvs format: Boca;ARQ;Trípodi, Emanuel;3,000,000;5
 * 
 * See players spreadsheet https://docs.google.com/spreadsheets/d/1hJvWDFMmVeiBLscwfkAMLAXJyiUwCELykzgBCJnfxqg/edit?usp=sharing
 * @author juan.fernandez
 */
@CsvDataType()
public class Player implements Gen {
    /**
     * Random player List's  index generator.
     */
    private static final Random RANDY = new Random();
    private static final String CSV_PATH = "/home/likewise-open/GLOBANT/juan.fernandez/Documents/GranDt.csv";
    
    /**
     * A complete map of Roles with all the players that are eligible.
     * 
     * Must be initialized before using {@link getRandomPlayer()}
     * Todo: make the List a Set with Random access
     */
    private static final Map<Role, SetUniqueList> ALL_PLAYERS = new EnumMap<>(Role.class);
    
    @CsvField(pos = 3)
    private final String name;
    
    @CsvField(pos = 2)
    private final Role role;
    
    @CsvField(pos = 1)
    private final String club;
    
    @CsvField(pos = 4)
    private final BigDecimal price;
    
    @CsvField(pos = 5)
    private final int score;
    
    /**
     * Initializes ALL_PLAYERS Roles.
     */
    static {
        for (Role r : Role.values()) {
            ALL_PLAYERS.put(r, SetUniqueList.decorate(new ArrayList<>()));
        }
        Deserializer deserializer = CsvIOFactory
                .createFactory(Player.class)
                .createDeserializer();
        StringReader reader = new StringReader(readFile(CSV_PATH, Charset.forName("UTF8")));
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            Player p = deserializer.next();
            ALL_PLAYERS.get(p.role).add(p);
            // do something useful with it
        }
        deserializer.close(true);
    }
    
    static String readFile(String path, Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Adds a player to the map of players that are eligible to form part of a Team.
     * 
     * @param p 
     */
    static void addPlayer(Player p) {
        ALL_PLAYERS.get(p.role).add(p);
    }
    
    static Player getRandomPlayer(Role role) {
        List<Player> pl = ALL_PLAYERS.get(role);
        return pl.get(RANDY.nextInt(pl.size()));
    }
    
    public Role getRole() {
        return this.role;
    }

    public Player() {
        this(null, null, null, null, 0);
    }
    
    public Player(Role role, String club, BigDecimal price, String name, int score) {
        this.role = role;
        this.club = club;
        this.price = price;
        this.name = name;
        this.score = score;
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    public String getClub() {
        return club;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return club + ":" + role + ":" + name + ":" + ":" + price + ":" + score; 
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Player)) {
            return false;
        }
        Player player = (Player) obj;
        return player.score == score && player.name.equals(name) && player.club.equals(club);
    }

    @Override
    public int hashCode() {
        return 31 * score + 17 * name.hashCode() + 13 * club.hashCode(); 
    }
    
    public enum Role {
        ARQ, DEF, VOL, DEL    
    }
}
