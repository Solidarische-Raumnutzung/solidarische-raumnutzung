package edu.kit.hci.soli.dto.form;

/**
 * Data class for editing the booking description.
 */
public class EditBookingDescriptionForm {
    private String description;

    /**
     * Constructs a new EditBookingDescriptionForm object with the specified description.
     *
     * @param description the new description of the booking
     */
    public EditBookingDescriptionForm(String description) {
        this.description = description;
    }

    /**
     * Returns the new description of the booking.
     *
     * @return the description of the booking
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the new description of the booking.
     *
     * @param description the new description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
