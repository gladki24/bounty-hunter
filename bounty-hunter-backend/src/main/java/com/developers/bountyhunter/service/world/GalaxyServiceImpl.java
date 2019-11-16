package com.developers.bountyhunter.service.world;

import com.developers.bountyhunter.model.world.Galaxy;
import com.developers.bountyhunter.repository.world.GalaxyRepository;
import com.developers.bountyhunter.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GalaxyServiceImpl extends BaseServiceImpl<Galaxy, Long> implements GalaxyService {

	private final GalaxyRepository galaxyRepository;

}