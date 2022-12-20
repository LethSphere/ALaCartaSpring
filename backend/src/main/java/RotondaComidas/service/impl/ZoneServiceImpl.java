package RotondaComidas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RotondaComidas.model.Zone;
import RotondaComidas.repository.ZoneRepository;
import RotondaComidas.service.services.ZoneService;

import java.util.List;


@Service
public class ZoneServiceImpl {

    @Autowired
    private ZoneRepository zoneRepository;
    public List<Zone> fetchAll() {
        return zoneRepository.findAll();
    }
}
