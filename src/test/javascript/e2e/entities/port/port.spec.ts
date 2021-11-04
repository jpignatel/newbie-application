import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PortComponentsPage } from './port.page-object';

const expect = chai.expect;

describe('Port e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let portComponentsPage: PortComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ports', async () => {
    await navBarPage.goToEntity('port');
    portComponentsPage = new PortComponentsPage();
    await browser.wait(ec.visibilityOf(portComponentsPage.title), 5000);
    expect(await portComponentsPage.getTitle()).to.eq('newbieApp.port.home.title');
    await browser.wait(ec.or(ec.visibilityOf(portComponentsPage.entities), ec.visibilityOf(portComponentsPage.noResult)), 1000);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
