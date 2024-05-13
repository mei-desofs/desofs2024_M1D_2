import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { RoleMySuffixService } from '../service/role-my-suffix.service';
import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixFormService } from './role-my-suffix-form.service';

import { RoleMySuffixUpdateComponent } from './role-my-suffix-update.component';

describe('RoleMySuffix Management Update Component', () => {
  let comp: RoleMySuffixUpdateComponent;
  let fixture: ComponentFixture<RoleMySuffixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roleFormService: RoleMySuffixFormService;
  let roleService: RoleMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RoleMySuffixUpdateComponent],
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
      .overrideTemplate(RoleMySuffixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoleMySuffixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roleFormService = TestBed.inject(RoleMySuffixFormService);
    roleService = TestBed.inject(RoleMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const role: IRoleMySuffix = { id: 456 };

      activatedRoute.data = of({ role });
      comp.ngOnInit();

      expect(comp.role).toEqual(role);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleMySuffix>>();
      const role = { id: 123 };
      jest.spyOn(roleFormService, 'getRoleMySuffix').mockReturnValue(role);
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRoleMySuffix).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roleService.update).toHaveBeenCalledWith(expect.objectContaining(role));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleMySuffix>>();
      const role = { id: 123 };
      jest.spyOn(roleFormService, 'getRoleMySuffix').mockReturnValue({ id: null });
      jest.spyOn(roleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: role }));
      saveSubject.complete();

      // THEN
      expect(roleFormService.getRoleMySuffix).toHaveBeenCalled();
      expect(roleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoleMySuffix>>();
      const role = { id: 123 };
      jest.spyOn(roleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ role });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
