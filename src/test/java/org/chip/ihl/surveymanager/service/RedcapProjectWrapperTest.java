/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.config.test.TestConfig;
import org.chip.ihl.surveymanager.entity.RedcapProject;
import org.chip.ihl.surveymanager.repository.RedcapProjectRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Matchers.eq;

@Test
@SpringApplicationConfiguration(classes = {TestConfig.class})
/**
 * tests RedcapProjectWrapper
 * ( assumes Spring Data Repo is working properly :) )
 */
public class RedcapProjectWrapperTest extends AbstractTestNGSpringContextTests {
    private final Logger logger = LoggerFactory.getLogger(RedcapProjectWrapperTest.class);

    private RedcapProjectService service;

    private static final int PROJECT1_ID = 77;
    private static final String PROJECT1_NAME = "myTestProject";
    private static final String PROJECT1_URL = "http://redcap.myinst.org/redcap?project_id="+ PROJECT1_ID;
    private static final String PROJECT1_TOKEN = "myProjectApiToken_1";
    private static final String PROJECT1_UPDATED_NAME = "myTestProject_Updated";
    private static final String PROJECT1_UPDATED_TOKEN = "myProjectApiToken_1_Updated";

    private static final int PROJECT2_ID = 88;
    private static final String PROJECT2_NAME = "myTestProject2";
    private static final String PROJECT2_URL = "http://redcap.myinst.org/redcap?project_id="+ PROJECT2_ID;
    private static final String PROJECT2_TOKEN = "myProjectApiToken_2";

    @Mock
    private RedcapProjectRepository mockRepository;

    RedcapProject project1;
    RedcapProject project1_updated;
//
//    @BeforeTest(alwaysRun = true)
//    public void setup() throws Exception {
//        service = new RedcapProjectWrapper(testRepository);
//    }
//
//    @AfterMethod
//    public void cleanup() {
//        testRepository.deleteAll();
//    }

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);

        project1 = getProject(PROJECT1_NAME, PROJECT1_URL, PROJECT1_TOKEN);
//        project1_updated = findProject(PROJECT1_ID, PROJECT1_UPDATED_NAME, PROJECT1_URL, PROJECT1_UPDATED_TOKEN);
        // setup mock behaviors
        Mockito.reset(mockRepository);
        // findProject
        Mockito.when(mockRepository.findFirstByUrl(PROJECT1_URL)).thenReturn(project1);
        // saveMock
        Mockito.when(mockRepository.save(eq(project1))).thenReturn(project1);
        // deleteMock
        Mockito.when(mockRepository.deleteByUrl(eq(PROJECT1_URL))).thenReturn(1L);

        service = new RedcapProjectWrapper(mockRepository);
    }

    /*** saveProject tests ***/
    @Test(groups = "static", expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidUrlSaveProjectThrowsException() {
        RedcapProject project = getProject(null);
        service.saveProject(project);
    }

    @Test(groups = "static", expectedExceptions = IllegalArgumentException.class)
    public void whenMissingTokenSaveProjectThrowsException() {
        RedcapProject project = getProject(PROJECT1_URL);
        project.setApiToken(null);
        service.saveProject(project);
    }

    @Test(groups = "static")
    public void whenValidSaveNewProject() {
        RedcapProject originalProject = getProject(PROJECT1_URL);
        originalProject = service.saveProject(originalProject);
        checkProject(originalProject);
    }

//    @Test(groups = "static")
//    public void whenValidUpdateProject() {
//        RedcapProject originalProject = newProject(1);
//        RedcapProject updatedProject = testRepository.saveAndFlush(originalProject);
//        String updatedToken = "updatedTokenApi";
//        String updatedUrl = "updatedUrl";
//        updatedProject.setApiToken(updatedToken);
//        updatedProject.setUrl(updatedUrl);
//        updatedProject = service.saveProject(originalProject);
//        Assert.assertEquals(updatedProject.getApiToken(), updatedToken);
//        Assert.assertEquals(updatedProject.getUrl(), updatedUrl);
//    }

    /*** findProject tests ***/
    @Test(groups = "static", expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidUrlFindProjectThrowsException() {
        RedcapProject project = service.findProject("");
    }

    @Test(groups = "static")
    public void whenValidFindProject() {
        RedcapProject retrievedProject = service.findProject(PROJECT1_URL);
        checkProject(retrievedProject);
    }

    /*** deleteProject tests ***/
    @Test(groups = "static", expectedExceptions = IllegalArgumentException.class)
    public void whenInvalidUrlDeleteProjectThrowsException() {
        long numDeleted = service.deleteProject("");
    }

    @Test(groups = "static")
    public void whenValidDeleteProject() {
        long numDeleted = service.deleteProject(PROJECT1_URL);
        Mockito.verify(mockRepository).deleteByUrl(PROJECT1_URL);
        Assert.assertEquals(1L, numDeleted);
    }

    // HELPERS
    private RedcapProject getProject(String name, String url, String token) {
        return new RedcapProject(name, url, token);
    }

    private RedcapProject getProject(String url) {
        return getProject(PROJECT1_NAME, url, PROJECT1_TOKEN);
    }

    private void checkProject(RedcapProject redcapProject) {
        Assert.assertNotNull(redcapProject);
        Assert.assertEquals(redcapProject.getName(), PROJECT1_NAME);
        Assert.assertEquals(redcapProject.getUrl(), PROJECT1_URL);
        Assert.assertEquals(redcapProject.getApiToken(), PROJECT1_TOKEN);
    }
}