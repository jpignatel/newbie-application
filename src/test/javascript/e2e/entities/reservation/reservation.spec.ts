import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReservationComponentsPage, ReservationDeleteDialog, ReservationUpdatePage } from './reservation.page-object';

const expect = chai.expect;

describe('Reservation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reservationComponentsPage: ReservationComponentsPage;
  let reservationUpdatePage: ReservationUpdatePage;
  let reservationDeleteDialog: ReservationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Reservations', async () => {
    await navBarPage.goToEntity('reservation');
    reservationComponentsPage = new ReservationComponentsPage();
    await browser.wait(ec.visibilityOf(reservationComponentsPage.title), 5000);
    expect(await reservationComponentsPage.getTitle()).to.eq('newbieApp.reservation.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(reservationComponentsPage.entities), ec.visibilityOf(reservationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Reservation page', async () => {
    await reservationComponentsPage.clickOnCreateButton();
    reservationUpdatePage = new ReservationUpdatePage();
    expect(await reservationUpdatePage.getPageTitle()).to.eq('newbieApp.reservation.home.createOrEditLabel');
    await reservationUpdatePage.cancel();
  });

  it('should create and save Reservations', async () => {
    const nbButtonsBeforeCreate = await reservationComponentsPage.countDeleteButtons();

    await reservationComponentsPage.clickOnCreateButton();

    await promise.all([
      reservationUpdatePage.setGuestFullNameInput('guestFullName'),
      reservationUpdatePage.setInitialDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      reservationUpdatePage.resStatusSelectLastOption(),
      reservationUpdatePage.setShipInput('ship'),
      reservationUpdatePage.setPortFromInput('portFrom'),
      reservationUpdatePage.setFromInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      reservationUpdatePage.setPortToInput('portTo'),
      reservationUpdatePage.setToInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
    ]);

    await reservationUpdatePage.save();
    expect(await reservationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reservationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Reservation', async () => {
    const nbButtonsBeforeDelete = await reservationComponentsPage.countDeleteButtons();
    await reservationComponentsPage.clickOnLastDeleteButton();

    reservationDeleteDialog = new ReservationDeleteDialog();
    expect(await reservationDeleteDialog.getDialogTitle()).to.eq('newbieApp.reservation.delete.question');
    await reservationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reservationComponentsPage.title), 5000);

    expect(await reservationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
