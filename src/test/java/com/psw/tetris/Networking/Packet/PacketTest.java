import com.psw.tetris.networking.GameClient;
import com.psw.tetris.networking.GameServer;
import org.junit.jupiter.api.Test;

public class Packet00LoginTest{
  @Test
  public void testConstructor1(){
  String username = "tiago";
  String data  = "00," + username;
    Packet00Login newP = new Packet00Login(data.getBytes());
    AssertNotNull(data);  
  }
}
