package edu.kit.hci.soli.dto.form;

/**
 * Data class for room creation or editing form data.
 */
public class EditOrCreateRoomForm {
    private Long target;
    private String name;
    private String description;
    private String location;

    /**
     * Constructs a new EditOrCreateRoomForm object with the specified parameters.
     *
     * @param target      the ID of the room to edit (null if creating a new room)
     * @param name        the name of the room
     * @param description a brief description of the room
     * @param location    the physical location of the room
     */
    public EditOrCreateRoomForm(Long target, String name, String description, String location) {
        this.target = target;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    /**
     * Returns the target room ID for editing.
     *
     * @return the target room ID, or null if creating a new room
     */
    public Long getTarget() {
        return this.target;
    }

    /**
     * Returns the name of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description of the room.
     *
     * @return the description of the room
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the location of the room.
     *
     * @return the location of the room
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the target room ID for editing.
     *
     * @param target the target room ID to set, or null if creating a new room
     */
    public void setTarget(Long target) {
        this.target = target;
    }

    /**
     * Sets the name of the room.
     *
     * @param name the name of the room to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the room.
     *
     * @param description the description of the room to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the location of the room.
     *
     * @param location the location of the room to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
