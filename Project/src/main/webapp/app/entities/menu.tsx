import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cart-my-suffix">
        Cart
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-my-suffix">
        Payment
      </MenuItem>
      <MenuItem icon="asterisk" to="/photo-my-suffix">
        Photo
      </MenuItem>
      <MenuItem icon="asterisk" to="/portfolio-my-suffix">
        Portfolio
      </MenuItem>
      <MenuItem icon="asterisk" to="/receipt-my-suffix">
        Receipt
      </MenuItem>
      <MenuItem icon="asterisk" to="/role-my-suffix">
        Role
      </MenuItem>
      <MenuItem icon="asterisk" to="/role-user-my-suffix">
        Role User
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-my-suffix">
        User
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
