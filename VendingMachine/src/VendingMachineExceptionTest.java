import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;


public class VendingMachineExceptionTest {

    @Test
    public void VendingMachineExceptionMessageTest(){

    //Arrange: create the message that print when the exception happens
    String message = "Invalid Code";

    //Act: create exception using message
    VendingMachineException exception = new VendingMachineException(message);

    //Assert: check that the exception is equal to the message
    assertEquals(message, exception.getMessage());
    }
}
