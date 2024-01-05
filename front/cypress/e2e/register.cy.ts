it('Register successfull', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
        body: {
          firstName: 'Mehdi',
          lastName: 'Mario',
          email: 'email@gamil.com',
          password: 'azertyuiop'
        },
      })

      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.get('input[formControlName=email]').type("email@gmail.com")
      cy.get('input[formControlName=firstName]').type("Mehdi")
      cy.get('input[formControlName=lastName]').type("Mario")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/login')
  
});

