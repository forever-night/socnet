package socnet.mappers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import socnet.config.TestConfig;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.util.TestUtil;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class ProfileMapperTest {
    @Autowired
    ProfileMapper profileMapper;
    
    private Profile profile;
    private ProfileDto profileDto;
    
    @Before
    public void setUp() {
        profile = TestUtil.generateProfile();
        profileDto = TestUtil.generateProfileDto();
    }
    
    @Test
    public void profileMapperNotNull() {
        Assert.assertNotNull(profileMapper);
    }
    
    @Test
    public void asProfileDto() {
        ProfileDto actual = profileMapper.asProfileDto(profile);
        
        Assert.assertNotNull(actual);
        Assert.assertEquals(profileDto, actual);
    }
    
    @Test
    public void asProfile() {
        Profile actual = profileMapper.asProfile(profileDto);
        
        Assert.assertNotNull(actual);
        Assert.assertEquals(profile, actual);
    }
}