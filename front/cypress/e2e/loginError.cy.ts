// Remove the import statement for 'cy' from the 'cypress' module
describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 404,
      body: 'Not Found',
    }).as('apiRequest');

    cy.get('mat-card-title').should('have.text', 'Login');

    cy.get('input[formControlName=email]').type("yoga")
    cy.get('input[formControlName=password]').type(`${"test"}{enter}{enter}`)

    cy.get('.error' ).should('be.visible').should('have.text', 'An error occurred');

    cy.get('mat-card-title').should('be.visible')

  })

});