package io.getarrays.server.implementation;

import io.getarrays.server.model.Server;
import io.getarrays.server.model.Status;
import io.getarrays.server.repo.ServerRepo;
import io.getarrays.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static org.springframework.data.domain.PageRequest.of;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j

public class ServerServiceImpl implements ServerService {
    private final ServerRepo serverRepo;
    @Override
    public Server create(Server server) {
        log.info("Saving new server :{}",server.getName());
        server.setImageUrl(setImageUrl());
        return serverRepo.save(server);
    }

    @Override
    public Server ping(String isIpAddress) throws IOException {
        log.info("Pinging server IP :{}",isIpAddress);
        Server server =serverRepo.findByIpAddress(isIpAddress);
        InetAddress address= InetAddress.getByName(isIpAddress);
        server.setStatus(address.isReachable(1000)? Status.SERVER_UP:Status.SERVER_DOWN);
        serverRepo.save(server);
        return serverRepo.save(server);
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all IP address");
        return serverRepo.findAll(of(0,limit)).toList();
    }

    @Override
    public Server get(long id) {
        log.info("Fetching server by Id:{}",id);
        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server :{}",server.getName());
        return serverRepo.save(server);
    }

    @Override
    public Boolean delete(long id) {
        log.info("Deleting server by Id :{}",id);
        serverRepo.deleteById(id);
        return Boolean.TRUE;
    }

    private String setImageUrl() {
        String[] imageNames = {"Server-1.png","Server-2.png","Server-3.png","Server-4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/Images/"+imageNames[new Random().nextInt(4)]).toUriString();
    }

}
