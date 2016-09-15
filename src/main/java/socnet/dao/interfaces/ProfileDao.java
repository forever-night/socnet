package socnet.dao.interfaces;

import socnet.entities.Profile;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ProfileDao {
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
     * Find all profiles.
     * */
    List<Profile> findAll();
    
    /**
     * Find all profiles with name or account login like a search pattern.
     *
     * @return map of login - profile pair or null if no result found
     * */
    Map<String, Profile> findAllLike(String searchPattern);
    
    /**
     * Check if profile is a follower of another profile.
     *
     * @param follower possible follower
     * @param owner profile that has a possible follower
     * */
    boolean checkIfFollows(Profile owner, Profile follower);
    
    /**
     * Given an array of profile ids, pick those who are followed by a given profile.
     *
     * @param followingIds array of ids of profiles that are possibly followed
     * @param profile profile that follows
     * @return list of profile ids or null if no match found
     * */
    List<Integer> pickFollowed(Profile profile, Integer[] followingIds);
    
    /**
     * Find profiles of followers of a given profile.
     *
     * @return set of profiles or null if not found
     * @throws javax.persistence.NoResultException if profile not found
     * */
    Set<Profile> findFollowers(Profile profile);
    
    /**
     * Find login - profile pairs of followers of a given profile.
     *
     * @return map of login - profile pairs or null if no result found
     * @throws javax.persistence.NoResultException if profile not found
     * */
    Map<String, Profile> findFollowersWithLogin(Profile profile);
    
    /**
     * Find profiles followed by a given profile.
     *
     * @return set of profiles or null if not found
     * @throws javax.persistence.NoResultException if profile not found
     * */
    Set<Profile> findFollowing(Profile profile);
    
    /**
     * Find login - profile pairs that are followed by a given profile.
     *
     * @return map of login - profile pairs or null if not found
     * @throws javax.persistence.NoResultException if profile not found
     * */
    Map<String, Profile> findFollowingWithLogin(Profile profile);

    /**
     * Persists a profile.
     * */
    Integer persist(Profile profile);

    /**
     * Updates a profile without updating its followers.
     * */
    Profile update(Profile profile);
    
    /**
     * Updates a profile with a given login without updating its followers.
     * */
    Profile update(Profile profile, String login);
    
    /**
     * Updates only followers of a given profile.
     * */
    Profile updateFollowers(Profile profile);
    
    /**
     * Updates only followers of a profile with a given login.
     * */
    Profile updateFollowers(Profile profile, String login);

    /**
     * Removes a profile.
     *
     * @throws javax.persistence.NoResultException if profile not found
     * */
    void remove(Profile profile);
}
