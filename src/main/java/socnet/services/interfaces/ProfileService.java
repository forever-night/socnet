package socnet.services.interfaces;

import socnet.dto.ProfileDto;
import socnet.entities.Profile;

import java.util.List;


public interface ProfileService {
    /**
     * Find a profile by id.
     *
     * @return Profile or null if not found
     * */
    Profile find(int id);
    
    /**
     * Find a profile by login.
     *
     * @return Profile
     * @throws javax.persistence.NoResultException if not found
     * */
    Profile findByLogin(String login);
    
    /**
     * Find a profileDto by login. Checks if current user follows a profile with a given login.
     *
     * @throws javax.persistence.NoResultException if not found
     * @throws java.nio.file.AccessDeniedException if user is not logged in
     * */
    ProfileDto findDtoByLogin(String login);
    
    /**
     * Find all profiles.
     * */
    List<Profile> findAll();
    
    /**
     * Find all profileDtos with name or account login like a search pattern.
     *
     * @return list of ProfileDto or null if no result found
     * */
    List<ProfileDto> findAllLike(String searchPattern);
    
    /**
     * Check if profile is a follower of another profile.
     *
     * @param follower possible follower
     * @param owner profile that has a possible follower
     * */
    boolean checkIfFollows(String owner, String follower);
    
    /**
     * Given a list of profile ids, pick those who are followed by current user.
     *
     * @param profileIdList list of ids of profiles that are possibly followed
     * @return list of profile ids or null if no match found
     * */
    List<Integer> pickFollowedByCurrentProfile(List<Integer> profileIdList);
    
    /**
     * Find profileDtos of followers of a given profile.
     *
     * @return list of ProfileDto or null if no result found
     * */
    List<ProfileDto> findFollowersWithLogin(Profile profile);
    
    /**
     * Find profileDtos of followers of a profile with a given login.
     *
     * @return list of ProfileDto or null if no result found
     * */
    List<ProfileDto> findFollowersWithLogin(String login);
    
    /**
     * Find profileDtos followed by a given profile.
     *
     * @return list of ProfileDto or null if not found
     * */
    List<ProfileDto> findFollowingWithLogin(Profile profile);
    
    /**
     * Find profileDtos followed by a profile with a given login.
     *
     * @return list of ProfileDto or null if not found
     * */
    List<ProfileDto> findFollowingWithLogin(String login);

    /**
     * Creates and persists a profile with a given Account id.
     * */
    Profile create(int accountId);
    
    /**
     * Updates a profile without updating its followers.
     * */
    Profile update(Profile profile);
    
    /**
     * Updates a profile with a given login without updating its followers.
     * */
    Profile update(Profile profile, String login);
    
    /**
     * Adds a profile follower to another profile.
     *
     * @param owner profile to follow
     * @param follower profile that follows
     * */
    Profile addFollower(Profile owner, Profile follower);
    
    /**
     * Removes a profile follower to another profile.
     *
     * @param owner profile to stop following
     * @param follower profile that stops following
     * */
    Profile removeFollower(Profile owner, Profile follower);

    /**
     * Removes a profile with a given id.
     * */
    void remove(Integer id);

    /**
     * Removes a given profile.
     * */
    void remove(Profile profile);
}
