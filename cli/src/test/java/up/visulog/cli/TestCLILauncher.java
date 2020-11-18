package up.visulog.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCLILauncher {
    // this class is for tests follow the Junit way to test your methods

    @Test
    public void testArgumentParser() {
        var config1 = CLILauncher.makeConfigFromCommandLineArgs(new String[] { ".", "--addPlugin=countCommits" });
        assertTrue(config1.isPresent());
        var config2 = CLILauncher.makeConfigFromCommandLineArgs(new String[] { "--nonExistingOption" });
        assertFalse(config2.isPresent());
        var config4 = CLILauncher.makeConfigFromCommandLineArgs(new String[] { "" });
        assertTrue(config4.isPresent());
    }

}
