package robotoer.project;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

/**
 */
public class WorkspaceTest {
  public Workspace generateTestWorkspace() {
    return new Workspace(Lists.<Module>newArrayList(), null);
  }

  @Test
  public void testGetPath() {
    Assert.assertEquals(null, generateTestWorkspace().getPath());
  }

  @Test
  public void testGetModules() {
    Assert.assertEquals(Lists.<Module>newArrayList(), generateTestWorkspace().getModules());
  }

  @Test
  public void testFromPath() {
    final Workspace expected = generateTestWorkspace();
    final Workspace actual = Workspace.fromPath("");
    Assert.assertEquals(expected, actual);
  }
}
