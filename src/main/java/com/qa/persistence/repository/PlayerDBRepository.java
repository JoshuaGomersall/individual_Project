package com.qa.persistence.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;
import com.qa.persistence.domain.Player;

import java.util.Collection;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.qa.util.JSONUtil;

@Transactional(SUPPORTS)
@Default
public class PlayerDBRepository implements PlayerRepository {

	@PersistenceContext(unitName = "primary")
	private EntityManager manager;

	@Inject
	private JSONUtil util;


	public String getAllPlayers() 
	{
		Query query = manager.createQuery("SELECT q FROM Player q");
		Collection<Player> PLAYER = (Collection<Player>) query.getResultList();
		return util.getJSONForObject(PLAYER);
	}

	@Transactional(REQUIRED)
	public String createPlayer(String player) 
	{
		Player aplayer = util.getObjectForJSON(player, Player.class);
		manager.persist(aplayer);
		return "{\"message\": \"player has been sucessfully added\"}";
	}
	
	@Transactional(REQUIRED)
	public String deletePlayer(Long id) 
	{
		if (manager.contains(manager.find(Player.class, id))) {

			manager.remove(manager.find(Player.class, id));
			return "{\"message\": \"player sucessfully deleted\"}";
		}
		return "{\"message\": \"No Player With That Id Can Be Found\"}";
	}

	public String getAPlayer(Long id) 
	{
		return util.getJSONForObject(manager.find(Player.class, id));
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public void setUtil(JSONUtil util) 
	{
		this.util = util;
	}

	public String updatePlayer(String player, Long id) {
		deletePlayer(id);
		createPlayer(player);
		return "{\"message\": \"player sucessfully updated\"}";
	}

	@Override
	public String getAPlayerbyname(String name) {
		Query query = manager.createQuery("SELECT a FROM Player a WHERE playerName LIKE '%"+name+"%'");
		Collection<Player> PLAYER = (Collection<Player>) query.getResultList();
		return util.getJSONForObject(PLAYER);
	}
}
