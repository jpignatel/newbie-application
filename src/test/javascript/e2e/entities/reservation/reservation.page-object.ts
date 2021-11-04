import { element, by, ElementFinder } from 'protractor';

export class ReservationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-reservation div table .btn-danger'));
  title = element.all(by.css('jhi-reservation div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ReservationUpdatePage {
  pageTitle = element(by.id('jhi-reservation-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  guestFullNameInput = element(by.id('field_guestFullName'));
  initialDateInput = element(by.id('field_initialDate'));
  resStatusSelect = element(by.id('field_resStatus'));
  shipInput = element(by.id('field_ship'));
  portFromInput = element(by.id('field_portFrom'));
  fromInput = element(by.id('field_from'));
  portToInput = element(by.id('field_portTo'));
  toInput = element(by.id('field_to'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setGuestFullNameInput(guestFullName: string): Promise<void> {
    await this.guestFullNameInput.sendKeys(guestFullName);
  }

  async getGuestFullNameInput(): Promise<string> {
    return await this.guestFullNameInput.getAttribute('value');
  }

  async setInitialDateInput(initialDate: string): Promise<void> {
    await this.initialDateInput.sendKeys(initialDate);
  }

  async getInitialDateInput(): Promise<string> {
    return await this.initialDateInput.getAttribute('value');
  }

  async setResStatusSelect(resStatus: string): Promise<void> {
    await this.resStatusSelect.sendKeys(resStatus);
  }

  async getResStatusSelect(): Promise<string> {
    return await this.resStatusSelect.element(by.css('option:checked')).getText();
  }

  async resStatusSelectLastOption(): Promise<void> {
    await this.resStatusSelect.all(by.tagName('option')).last().click();
  }

  async setShipInput(ship: string): Promise<void> {
    await this.shipInput.sendKeys(ship);
  }

  async getShipInput(): Promise<string> {
    return await this.shipInput.getAttribute('value');
  }

  async setPortFromInput(portFrom: string): Promise<void> {
    await this.portFromInput.sendKeys(portFrom);
  }

  async getPortFromInput(): Promise<string> {
    return await this.portFromInput.getAttribute('value');
  }

  async setFromInput(from: string): Promise<void> {
    await this.fromInput.sendKeys(from);
  }

  async getFromInput(): Promise<string> {
    return await this.fromInput.getAttribute('value');
  }

  async setPortToInput(portTo: string): Promise<void> {
    await this.portToInput.sendKeys(portTo);
  }

  async getPortToInput(): Promise<string> {
    return await this.portToInput.getAttribute('value');
  }

  async setToInput(to: string): Promise<void> {
    await this.toInput.sendKeys(to);
  }

  async getToInput(): Promise<string> {
    return await this.toInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ReservationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reservation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reservation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
