import cart from 'app/entities/cart-my-suffix/cart-my-suffix.reducer';
import payment from 'app/entities/payment-my-suffix/payment-my-suffix.reducer';
import photo from 'app/entities/photo-my-suffix/photo-my-suffix.reducer';
import portfolio from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.reducer';
import receipt from 'app/entities/receipt-my-suffix/receipt-my-suffix.reducer';
import role from 'app/entities/role-my-suffix/role-my-suffix.reducer';
import roleUser from 'app/entities/role-user-my-suffix/role-user-my-suffix.reducer';
import user from 'app/entities/user-my-suffix/user-my-suffix.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cart,
  payment,
  photo,
  portfolio,
  receipt,
  role,
  roleUser,
  user,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
