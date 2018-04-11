package com.ai.cms.star.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.star.entity.Club;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.cms.star.repository.StarRepository;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class ClubService extends AbstractService<Club, Long> {

	@Autowired
	private ClubRepository clubRepository;

	@Autowired
	private StarRepository starRepository;

	@Override
	public AbstractRepository<Club, Long> getRepository() {
		return clubRepository;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteClub(Club club) {
		if (club != null) {
			clubRepository.delete(club);
		}
	}

}