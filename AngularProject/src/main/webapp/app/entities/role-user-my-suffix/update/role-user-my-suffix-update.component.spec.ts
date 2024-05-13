import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUserMySuffix } from 'app/entities/user-my-suffix/user-my-suffix.model';
import { UserMySuffixService } from 'app/entities/user-my-suffix/service/user-my-suffix.service';
import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { RoleMySuffixService } from 'app/entities/role-my-suffix/service/role-my-suffix.service';
import { IRoleUserMySuffix } from '../role-user-my-suffix.model';
import { RoleUserMySuffixService } from '../service/role-user-my-suffix.service';
import { RoleUserMySuffixFormService } from './role-user-my-suffix-form.service';

import { RoleUserMySuffixUpdateComponent } from './role-user-my-suffix-update.component';

describe('RoleUserMySuffix Management Update Component', () => {
  let comp: RoleUserMySuffixUpdateComponent;
  let fixture: ComponentFixture<RoleUserMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleUserFormService: RoleUserMySuffixFormService;
  let roleUserService: RoleUserMySuffixService;
  let userService: UserMySuffixService;
  let roleService: RoleMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RoleUserMySuffixUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RoleUserMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoleUserMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleUserFormService = TestBed.inject(RoleUserMySuffixFormService);
    roleUserService = TestBed.inject(RoleUserMySuffixService);
    userService = TestBed.inject(UserMySuffixService);
    roleService = TestBed.inject(RoleMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call userId query and add missing value', () => {
      const roleUser: IRoleUserMySuffix = { id: 456 };
      const userId: IUserMySuffix = { id: 8075 };
      roleUser.userId = userId;

      const userIdCollection: IUserMySuffix[] = [{ id: 4595 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userIdCollection })));
      const expectedCollection: IUserMySuffix[] = [userId, ...userIdCollection];
      jest.spyOn(userService, 'addUserMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roleUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserMySuffixToCollectionIfMissing).toHaveBeenCalledWith(userIdCollection, userId);
      expect(comp.userIdsCollection).toEqual(expectedCollection);
    });

    it('Should call roleId query and add missing value', () => {
      const roleUser: IRoleUserMySuffix = { id: 456 };
      const roleId: IRoleMySuffix = { id: 25033 };
      roleUser.roleId = roleId;

      const roleIdCollection: IRoleMySuffix[] = [{ id: 4914 }];
      jest.spyOn(roleService, 'query').mockReturnValue(of(new HttpResponse({ body: roleIdCollection })));
      const expectedCollection: IRoleMySuffix[] = [roleId, ...roleIdCollection];
      jest.spyOn(roleService, 'addRoleMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roleUser });
      comp.ngOnInit();

      expect(roleService.query).toHaveBeenCalled();
      expect(roleService.addRoleMySuffixToCollectionIfMissing).toHaveBeenCalledWith(roleIdCollection, roleId);
      expect(comp.roleIdsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roleUser: IRoleUserMySuffix = { id: 456 };
      const userId: IUserMySuffix = { id: 28456 };
      roleUser.userId = userId;
      const roleId: IRoleMySuffix = { id: 437 };
      roleUser.roleId = roleId;

      activatedRoute.data = of({ roleUser });
      comp.ngOnInit();

      expect(comp.userIdsCollection).toContain(userId);
      expect(comp.roleIdsCollection).toContain(roleId);
      expect(comp.roleUser).toEqual(roleUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleUserMySuffix>>();
      const roleUser = { id: 123 };
      jest.spyOn(roleUserFormService, 'getRoleUserMySuffix').mockReturnValue(roleUser);
      jest.spyOn(roleUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roleUser }));
      saveSubject.complete();

      // THEN
      expect(roleUserFormService.getRoleUserMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleUserService.update).toHaveBeenCalledWith(expect.objectContaining(roleUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleUserMySuffix>>();
      const roleUser = { id: 123 };
      jest.spyOn(roleUserFormService, 'getRoleUserMySuffix').mockReturnValue({ id: null });
      jest.spyOn(roleUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roleUser }));
      saveSubject.complete();

      // THEN
      expect(roleUserFormService.getRoleUserMySuffix).toHaveBeenCalled();
      expect(roleUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleUserMySuffix>>();
      const roleUser = { id: 123 };
      jest.spyOn(roleUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roleUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserMySuffix', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUserMySuffix');
        comp.compareUserMySuffix(entity, entity2);
        expect(userService.compareUserMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoleMySuffix', () => {
      it('Should forward to roleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roleService, 'compareRoleMySuffix');
        comp.compareRoleMySuffix(entity, entity2);
        expect(roleService.compareRoleMySuffix).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
