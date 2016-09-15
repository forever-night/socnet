package socnet.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Profile;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;


@Component
public class ProfileDaoImpl implements ProfileDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Profile find(int id) {
        return em.find(Profile.class, id);
    }
    
    @Override
    public Profile findByLogin(String login) {
        Query query = em.createQuery("from Profile p where p.id = (" +
                "select a.id from Account a where a.login = :login)");
        query.setParameter("login", login);
        
        return (Profile) query.getSingleResult();
    }
    
    @Override
    public List<Profile> findAll() {
        return em.createQuery("from Profile", Profile.class).getResultList();
    }
    
    @Override
    @Transactional
    public Map<String, Profile> findAllLike(String searchPattern) {
        Query query = em.createQuery("select new map(a.login as login, p.id as id, " +
                "p.name as name, p.country as country, p.phone as phone, p.info as info, p.currentCity as city, " +
                "p.dateOfBirth as dateOfBirth) " +
                "from Profile as p, Account as a " +
                "where p.id = a.id " +
                "and (a.login like lower(:login) or p.name like lower(:login))");
        query.setParameter("login", "%" + searchPattern + "%");
        
        List<Map<String, Object>> queryResultList = query.getResultList();
        
        if (queryResultList.isEmpty())
            return null;
        else
            return mapResultListAsLoginProfileMap(queryResultList);
    }
    
    @Override
    public boolean checkIfFollows(Profile owner, Profile follower) {
        Query query = em.createQuery("select count(f) from Profile p join p.followers f " +
                "where p.id = :ownerId and f.id = :followerId");
        
        query.setParameter("ownerId", owner.getId());
        query.setParameter("followerId", follower.getId());
        
        Long count = (Long) query.getSingleResult();
        
        return count > 0;
    }
    
    @Override
    public List<Integer> pickFollowed(Profile profile, Integer[] followingIds) {
        Query query = em.createQuery("select p.id from Profile p join p.followers f " +
                "where f.id = :id and p.id in :followingIds");
        
        query.setParameter("id", profile.getId());
        query.setParameter("followingIds", Arrays.asList(followingIds));
        
        List<Integer> queryResultList = query.getResultList();
        
        if (queryResultList == null || queryResultList.isEmpty())
            return null;
        else
            return new ArrayList<>(queryResultList);
    }
    
    @Override
    public Set<Profile> findFollowers(Profile profile) {
        Profile persisted = em.find(Profile.class, profile.getId());
        
        if (persisted == null)
            throw new NoResultException();
        
        Set<Profile> followers = persisted.getFollowers();
        
        return followers == null || followers.isEmpty() ? null : new HashSet<>(followers);
    }
    
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Map<String, Profile> findFollowersWithLogin(Profile profile) {
        Profile persisted = em.find(Profile.class, profile.getId());
        
        if (persisted == null)
            throw new NoResultException();
        
        Query query = em.createQuery("select new map(a.login as login, f.id as id, " +
                "f.name as name, f.country as country, f.phone as phone, f.info as info, f.currentCity as city, " +
                "f.dateOfBirth as dateOfBirth) " +
                "from Profile p join p.followers f, Account a " +
                "where f.id = a.id and p.id = :id");
        
        query.setParameter("id", persisted.getId());
        List<Map<String, Object>> queryResultList = query.getResultList();
        
        if (queryResultList == null || queryResultList.isEmpty())
            return null;
        else
            return mapResultListAsLoginProfileMap(queryResultList);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Set<Profile> findFollowing(Profile profile) {
        Profile persisted = em.find(Profile.class, profile.getId());
        
        if (persisted == null)
            throw new NoResultException();
        
        Query query = em.createQuery("select p from Profile p inner join p.followers f where f.id = :id");
        query.setParameter("id", profile.getId());
        
        List<Profile> followers = query.getResultList();
        
        return followers == null || followers.isEmpty() ? null : new HashSet<>(followers);
    }
    
    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Map<String, Profile> findFollowingWithLogin(Profile profile) {
        Profile persisted = em.find(Profile.class, profile.getId());
        
        if (persisted == null)
            throw new NoResultException();
        
        Query query = em.createQuery("select new map(a.login as login, p.id as id, " +
                "p.name as name, p.country as country, p.phone as phone, p.info as info, p.currentCity as city, " +
                "p.dateOfBirth as dateOfBirth) " +
                "from Profile p join p.followers f, Account a " +
                "where p.id = a.id and f.id = :id");
        
        query.setParameter("id", profile.getId());
        List<Map<String, Object>> queryResultList = query.getResultList();
        
        if (queryResultList == null || queryResultList.isEmpty())
            return null;
        else
            return mapResultListAsLoginProfileMap(queryResultList);
    }
    
    @Override
    public Integer persist(Profile profile) {
        em.persist(profile);
        em.flush();

        return profile.getId();
    }

    @Override
    @Transactional
    public Profile update(Profile profile) {
        Profile old = em.find(Profile.class, profile.getId());

        old.setName(profile.getName());
        old.setDateOfBirth(profile.getDateOfBirth());
        old.setCountry(profile.getCountry());
        old.setCurrentCity(profile.getCurrentCity());
        old.setPhone(profile.getPhone());
        old.setInfo(profile.getInfo());

        return em.merge(old);
    }
    
    @Override
    @Transactional
    public Profile update(Profile profile, String login) {
        Profile old;
        
        if (profile.getId() != null)
            old = em.find(Profile.class, profile.getId());
        else
            old = findByLogin(login);
    
        old.setName(profile.getName());
        old.setDateOfBirth(profile.getDateOfBirth());
        old.setCountry(profile.getCountry());
        old.setCurrentCity(profile.getCurrentCity());
        old.setPhone(profile.getPhone());
        old.setInfo(profile.getInfo());
        
        return em.merge(old);
    }
    
    @Override
    @Transactional
    public Profile updateFollowers(Profile profile) {
        Profile old = em.find(Profile.class, profile.getId());
        old.setFollowers(profile.getFollowers());
        
        return em.merge(old);
    }
    
    @Override
    @Transactional
    public Profile updateFollowers(Profile profile, String login) {
        Profile old;
        
        if (profile.getId() != null)
            old = em.find(Profile.class, profile.getId());
        else
            old = findByLogin(login);
        
        old.setFollowers(profile.getFollowers());
        
        return em.merge(old);
    }

    @Override
    @Transactional
    public void remove(Profile profile) {
        Profile old = em.find(Profile.class, profile.getId());
        
        if (old == null)
            throw new NoResultException();
        
        Set<Profile> oldFollowerSet = findFollowers(old);
        Set<Profile> oldFollowingSet = findFollowing(old);

        if (oldFollowerSet != null && !oldFollowerSet.isEmpty()) {
            oldFollowerSet.clear();
            updateFollowers(old);
        }

        if (oldFollowingSet != null && !oldFollowingSet.isEmpty())
            for (Profile following : oldFollowingSet) {
                following.getFollowers().remove(old);
                em.merge(following);
            }
            
        em.remove(old);
    }
    
    private Map<String, Profile> mapResultListAsLoginProfileMap(List<Map<String, Object>> queryResultList) {
        Map<String, Profile> resultMap = new HashMap<>();
    
        for (Map<String, Object> map : queryResultList) {
            String resultLogin = (String) map.get("login");
        
            Profile profile = new Profile();
            
            Integer id = (Integer) map.get("id");
            
            if (id != null)
                profile.setId(id);
            
            profile.setName((String) map.get("name"));
            profile.setCountry((String) map.get("country"));
            profile.setPhone((String) map.get("phone"));
            profile.setInfo((String) map.get("info"));
            profile.setCurrentCity((String) map.get("city"));
            profile.setDateOfBirth((Date) map.get("dateOfBirth"));
        
            resultMap.put(resultLogin, profile);
        }
    
        return resultMap;
    }
}
