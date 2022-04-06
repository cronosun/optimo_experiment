package ch.bedag.sie.dij.optimo.model;

import ch.bedag.sie.dij.optimo.dabbawala.DabRole;

public enum KnownDabRoles {
    APPROVE_RECHNUNG(new DabRole("DJI_APPROVE_RECHNUNG"));

    private final DabRole role;

    KnownDabRoles(DabRole role) {
        this.role = role;
    }

    public DabRole getRole() {
        return role;
    }
}
